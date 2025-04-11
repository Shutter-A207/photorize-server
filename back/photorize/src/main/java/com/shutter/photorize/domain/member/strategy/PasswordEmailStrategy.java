package com.shutter.photorize.domain.member.strategy;

import com.shutter.photorize.domain.member.entity.ProviderType;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.global.constant.StringFormat;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.infra.redis.service.RedisAuthCodeAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordEmailStrategy implements EmailCodeStrategy {

	private final RedisAuthCodeAdapter redisAuthCodeAdapter;
	private final MemberRepository memberRepository;

	@Override
	public void save(String email, String code) {
		if (!memberRepository.existsByEmailAndProvider(email, ProviderType.BASIC)) {
			throw new PhotorizeException(ErrorType.USER_NOT_FOUND);
		}

		String key = generateSaveKey(email);
		if (redisAuthCodeAdapter.hasKey(key)) {
			throw new PhotorizeException(ErrorType.EMAIL_IN_PROGRESS);
		}

		redisAuthCodeAdapter.saveOrUpdate(key, code, 10);
	}

	@Override
	public boolean isProcessingEmail(String email) {
		String key = generateSaveKey(email);
		return redisAuthCodeAdapter.hasKey(key) && redisAuthCodeAdapter.getExpireTime(key) > 0;
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
		redisAuthCodeAdapter.saveOrUpdate(generateAvailableKey(email), String.valueOf(true), 300);
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
