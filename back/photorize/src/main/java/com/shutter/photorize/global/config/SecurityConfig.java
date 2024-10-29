package com.shutter.photorize.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shutter.photorize.global.jwt.JwtFilter;
import com.shutter.photorize.global.jwt.JwtUtil;
import com.shutter.photorize.global.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtil jwtUtil;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
	}

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//csrf disable
		//세션방식에서는 세션이 항상 고정되기때문에 CSRF 공격을 필수적으로 방어해야함.
		// JWT는 세션을 stateless 상태로 관리하기 때문에 CSRF에 대한 공격을 방어하지않아도되어서 disable로 둔다.
		http
			.csrf((auth) -> auth.disable());

		//From 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//http basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		// 경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/login", "/", "/join").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.anyRequest().authenticated());

		// JwtFilter 등록
		http
			.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
				UsernamePasswordAuthenticationFilter.class);

		// 세션 설정
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
