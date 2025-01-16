package com.shutter.photorize.infra.redis.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisAuthCodeAdapter implements RedisAdapter {

	private final RedisTemplate<String, Object> redisTemplate;

	// key-value를 redis에 저장
	@Override
	public void saveOrUpdate(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	// 만료 시간 설정 저장 메소드
	public void saveOrUpdate(String key, String value, int min) {
		redisTemplate.opsForValue().set(key, value, min, TimeUnit.MINUTES);
	}

	// 값 조회 메소드
	@Override
	public Optional<String> getValue(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		if (value instanceof String) {
			return Optional.of((String)value);
		}
		return Optional.empty();
	}

	// 주어진 key에 해당하는 데이터 삭제
	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	public Long getExpireTime(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

}
