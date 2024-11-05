package com.shutter.photorize.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.shutter.photorize.global.jwt.filter.JwtFilter;
import com.shutter.photorize.global.jwt.handler.JwtAccessDeniedHandler;
import com.shutter.photorize.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtUtil jwtUtil;

	//AuthenticationManager Bean 등록

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//http basic 인증 방식 disable
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
						"/api/v1/auth/**"
					)
					.permitAll()
					.anyRequest().authenticated()
			)
			// JwtFilter 등록
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
			// 세션 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptionHandling ->
				exceptionHandling
					.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(authenticationEntryPoint));

		return http.build();
	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
			List.of("https://localhost:5173", "http://localhost:5173", "https://photorize.co.kr"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(List.of("Authorization"));
		configuration.setMaxAge(3600L);

		configuration.setExposedHeaders(List.of("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
