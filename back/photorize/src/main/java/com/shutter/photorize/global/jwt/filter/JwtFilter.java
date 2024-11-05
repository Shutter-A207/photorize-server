package com.shutter.photorize.global.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shutter.photorize.global.jwt.service.CustomUserDetailService;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailService userDetailService;

	public JwtFilter(JwtUtil jwtUtil, CustomUserDetailService userDetailService) {

		this.jwtUtil = jwtUtil;
		this.userDetailService = userDetailService;
	}

	// 유효한 토큰이면 -> 사용자 정보 조회 -> 조회된 사용자 정보로 인증 객체 생성 -> SecurityContext에 인증 정보 설정
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			String token = resolveToken(request);

			// JWT 유효성 검증
			if (StringUtils.hasText(token) && jwtUtil.validation(token)) {
				String email = jwtUtil.getEmail(token);

				UserDetails userDetails = userDetailService.loadUserByUsername(email);

				if (userDetails != null) {
					// UserDetails, Password 정보를 기반으로 접근 권한을 가지고 있는 Token 생성
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null);

					// Security Context 해당 접근 권한 정보 설정
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			// 다음 필터로 넘기기
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			// JWT가 만료된 경우
			log.error("Expired JWT token", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
		} catch (SecurityException | MalformedJwtException e) {
			// JWT가 잘못된 서명이거나 형식이 잘못된 경우
			log.error("Invalid JWT token", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
		} catch (Exception e) {
			log.error("Unexpected error", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error");
		}

	}

	// Request Header에서 토큰 조회 및 Bearer 문자열 제거 후 반환하는 메소드
	private String resolveToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		// Token 정보 존재 여부 및 Bearer 토큰인지 확인
		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7);
		}

		return null;
	}
}
