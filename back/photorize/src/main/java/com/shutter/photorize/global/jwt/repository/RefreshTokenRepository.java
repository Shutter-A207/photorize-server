package com.shutter.photorize.global.jwt.repository;

import static com.shutter.photorize.global.constant.StringFormat.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${spring.jwt.expire.time.refresh}")
	private long expireRefreshToken;

	public void save(String userEmail, String refreshToken) {
		String key = generateKey(userEmail);
		redisTemplate.opsForValue().set(key, refreshToken);
		setExpiration(key);
	}

	public Optional<String> findByEmail(String userEmail) {
		String key = generateKey(userEmail);
		String value = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(value);
	}

	public void deleteByEmail(String userEmail) {
		String key = generateKey(userEmail);
		redisTemplate.delete(key);
	}

	private String generateKey(String userEmail) {
		return REFRESH_TOKEN_PREFIX + userEmail;
	}

	private void setExpiration(String key) {
		redisTemplate.expire(key, expireRefreshToken, TimeUnit.DAYS);
	}
}
