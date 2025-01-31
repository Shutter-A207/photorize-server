package com.shutter.photorize.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shutter.photorize.domain.member.service.EmailService;
import com.shutter.photorize.domain.member.service.OauthService;
import com.shutter.photorize.global.jwt.filter.JwtFilter;
import com.shutter.photorize.global.jwt.filter.LoginFilter;
import com.shutter.photorize.global.jwt.handler.JwtAccessDeniedHandler;
import com.shutter.photorize.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.shutter.photorize.global.jwt.handler.OAuthLoginFailureHandler;
import com.shutter.photorize.global.jwt.handler.OAuthLoginSuccessHandler;
import com.shutter.photorize.global.jwt.service.TokenService;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final OAuthLoginSuccessHandler loginSuccessHandler;
	private final OAuthLoginFailureHandler loginFailureHandler;
	private final JwtUtil jwtUtil;
	private final CorsConfig corsConfig;
	private final TokenService tokenService;

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
		JwtFilter jwtFilter,
		EmailService emailService, OauthService oauthService) throws
		Exception {

		//http basic 인증 방식 disable
		http
			.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
			//csrf disable
			//세션방식에서는 세션이 항상 고정되기때문에 CSRF 공격을 필수적으로 방어해야함.
			// JWT는 세션을 stateless 상태로 관리하기 때문에 CSRF에 대한 공격을 방어하지않아도되어서 disable로 둔다.
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			//From 로그인 방식 disable
			.formLogin(AbstractHttpConfigurer::disable)
			// 경로별 인가 작업
			.authorizeHttpRequests(authorize ->
				authorize
					.requestMatchers(
						"/api/v1/auth/**",
						"api/v1/health",
						"/api/v1/members/checkNickname",
						"/oauth2/**"
					)
					.permitAll()
					.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorization -> authorization
					.baseUri("/oauth2/authorize/**")
				)
				.redirectionEndpoint(redirection -> redirection
					.baseUri("/oauth2/login/code/*"))
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
					.userService(oauthService))
				.successHandler(loginSuccessHandler)
				.failureHandler(loginFailureHandler)
			)
			.addFilter(new LoginFilter(authenticationManager, jwtUtil, tokenService))
			// JwtFilter 등록
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			// 세션 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptionHandling ->
				exceptionHandling
					.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(authenticationEntryPoint));

		return http.build();
	}
}
