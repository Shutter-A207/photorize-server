package com.shutter.photorize.infra.redis.service;

import java.util.Optional;

public interface RedisAdapter {
	void saveOrUpdate(String key, String value);

	Optional<String> getValue(String key);

	void delete(String key);
}
