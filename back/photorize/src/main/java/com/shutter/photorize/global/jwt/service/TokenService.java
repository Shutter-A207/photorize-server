package com.shutter.photorize.global.jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.jwt.model.TokenDto;
import com.shutter.photorize.global.jwt.repository.RefreshTokenRepository;
import com.shutter.photorize.global.jwt.util.JwtUtil;
import com.shutter.photorize.global.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	public TokenDto createToken(String userEmail) {

		String accessToken = jwtUtil.createAccessToken(userEmail);
		String refreshToken = jwtUtil.createRefreshToken(userEmail);

		refreshTokenRepository.save(userEmail, refreshToken);

		return TokenDto.of(accessToken, refreshToken);
	}

	@Transactional
	public TokenDto reissueToken(String refreshToken, HttpServletResponse response) {
		try {
			TokenDto newToken = reissueTokenProcess(refreshToken);
			response.addHeader("Authorization", "Bearer " + newToken.getAccessToken());
			response.addHeader("Refresh-Token", newToken.getRefreshToken());

			return newToken;
		} catch (Exception e) {
			throw new PhotorizeException(ErrorType.TOKEN_REISSUE_FAILED);
		}
	}

	@Transactional
	public TokenDto reissueOAuthToken(String refreshToken, HttpServletResponse response) {
		try {
			TokenDto newToken = reissueTokenProcess(refreshToken);
			CookieUtil.setCookie(response, "access_token", newToken.getAccessToken(), 60 * 60);
			CookieUtil.setCookie(response, "refresh_token", newToken.getRefreshToken(), 60 * 60 * 24 * 14);
			return newToken;
		} catch (Exception e) {
			throw new PhotorizeException(ErrorType.TOKEN_REISSUE_FAILED);
		}
	}

	private TokenDto reissueTokenProcess(String refreshToken) {
		validateRefreshToken(refreshToken);
		String userEmail = jwtUtil.getEmail(refreshToken);
		validateRedisRefreshToken(userEmail, refreshToken);
		refreshTokenRepository.deleteByEmail(userEmail);
		return createToken(userEmail);
	}

	private void validateRefreshToken(String refreshToken) {
		if (!jwtUtil.validation(refreshToken)) {
			throw new PhotorizeException(ErrorType.EXPIRED_REFRESH_TOKEN);
		}
	}

	private void validateRedisRefreshToken(String userEmail, String refreshToken) {
		String redisRefreshToken = refreshTokenRepository.findByEmail(userEmail)
			.orElseThrow(() -> new PhotorizeException(ErrorType.INVALID_REFRESH_TOKEN));

		if (!redisRefreshToken.equals(refreshToken)) {
			throw new PhotorizeException(ErrorType.INVALID_REFRESH_TOKEN);
		}
	}
}
