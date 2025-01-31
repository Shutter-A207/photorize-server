package com.shutter.photorize.global.jwt.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shutter.photorize.global.jwt.model.TokenDto;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	private static final String KEY_PREFIX = "refreshToken:";

	public TokenDto createToken(String userEmail) {

		String accessToken = jwtUtil.createAccessToken(userEmail);
		String refreshToken = jwtUtil.createRefreshToken(userEmail);

		redisTemplate.opsForValue().set(KEY_PREFIX + userEmail, accessToken);
		redisTemplate.expire(KEY_PREFIX + refreshToken, 14, TimeUnit.DAYS);

		return TokenDto.of(accessToken, refreshToken);
	}
}
