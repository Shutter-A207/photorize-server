package com.shutter.photorize.global.jwt.filter;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shutter.photorize.global.jwt.model.CustomUserDetails;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/v1/auth/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		// 클라이언트 요청에서 username, password 추출
		String username = request.getParameter("email");
		String password = request.getParameter("password");

		// 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함.
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
			null);

		// authenticationManager로 던지면, 검증을 해줌. (DB에서 회원정보 받아와서 검증)
		return authenticationManager.authenticate(authToken);

	}

	// 로그인 성공시 실행하는 메서드 (여기서 JWT을 발급하면 됨.)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) {

		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String accessToken = jwtUtil.createAccessToken(username);
		String refreshToken = jwtUtil.createRefreshToken(username);

		response.addHeader("Authorization", "Bearer " + accessToken);
	}

	// 로그인 실패시 실행하는 메서드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {

		response.setStatus(401);
	}
}
