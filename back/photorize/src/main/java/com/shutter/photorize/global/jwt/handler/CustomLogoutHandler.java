package com.shutter.photorize.global.jwt.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.shutter.photorize.global.jwt.repository.RefreshTokenRepository;
import com.shutter.photorize.global.jwt.util.JwtUtil;
import com.shutter.photorize.global.util.CookieUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		// 1. refresh_token 쿠키 추출
		String refreshToken = CookieUtil.resolveCookie(request, "refresh_token")
			.map(Cookie::getValue)
			.orElse(null);

		// 2. 토큰 유효하면 Redis에서 삭제
		if (refreshToken != null && jwtUtil.validation(refreshToken)) {
			String userEmail = jwtUtil.getEmail(refreshToken);
			refreshTokenRepository.deleteByEmail(userEmail);
			log.info("userEmail : {}", userEmail);
		}

		// 3. 쿠키 삭제 (access_token, refresh_token)
		CookieUtil.deleteCookie(request, response, "access_token");
		CookieUtil.deleteCookie(request, response, "refresh_token");
	}
}
