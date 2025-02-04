package com.shutter.photorize.global.jwt.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.dto.CustomOAuthUser;
import com.shutter.photorize.global.jwt.model.TokenDto;
import com.shutter.photorize.global.jwt.service.TokenService;
import com.shutter.photorize.global.jwt.util.JwtUtil;
import com.shutter.photorize.global.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final TokenService tokenService;

	@Transactional
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuthUser principal = (CustomOAuthUser)authentication.getPrincipal();
		String userEmail = principal.getName();

		TokenDto tokens = tokenService.createToken(userEmail);
		String accessToken = tokens.getAccessToken();
		String refreshToken = tokens.getRefreshToken();

		log.debug("accessToken: {}", accessToken);

		CookieUtil.setCookie(response, "access_token", accessToken, 60 * 60);
		CookieUtil.setCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 14);

		getRedirectStrategy().sendRedirect(request, response, "https://photorize.co.kr/home");

	}
}
