package com.shutter.photorize.domain.member.strategy;

import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.global.constant.StringFormat;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.infra.redis.service.RedisAuthCodeAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordAuthStrategy implements AuthCodeStrategy {

	private final RedisAuthCodeAdapter redisAuthCodeAdapter;
	private final MemberRepository memberRepository;

	@Override
	public void save(String email, String code) {
		if (!memberRepository.existsByEmail(email)) {
			throw new PhotorizeException(ErrorType.USER_NOT_FOUND);
		}
		redisAuthCodeAdapter.saveOrUpdate(generateSaveKey(email), code, 5);
	}

	@Override
	public boolean validAuthCode(String email, String code) {
		String getCode = redisAuthCodeAdapter.getValue(generateSaveKey(email))
			.orElseThrow(() -> new PhotorizeException(ErrorType.EXPIRED_EMAIL_CODE));

		log.info("validAuthCode:{}", getCode);

		return getCode.equals(code);
	}

	@Override
	public void pushAvailableEmail(String email) {
		redisAuthCodeAdapter.saveOrUpdate(generateAvailableKey(email), String.valueOf(true), 5);
	}

	@Override
	public void checkAvailableEmail(String email) {
		redisAuthCodeAdapter.getValue(generateAvailableKey(email))
			.orElseThrow(() -> new PhotorizeException(ErrorType.INVALID_EMAIL_VERIFIED));
	}

	private String generateSaveKey(String email) {
		return StringFormat.PASSWORD_AUTH_PREFIX + email;
	}

	private String generateAvailableKey(String email) {
		return StringFormat.PASSWORD_CHANGE_AVAILABLE_PREFIX + email;
	}
}
