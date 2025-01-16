package com.shutter.photorize.domain.member.strategy;

import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.global.constant.StringFormat;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.infra.redis.service.RedisAuthCodeAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignInEmailStrategy implements EmailCodeStrategy {

	private final RedisAuthCodeAdapter redisAuthCodeAdapter;
	private final MemberRepository memberRepository;

	// 인증 코드를 5분동안 redis에 저장
	@Override
	public void save(String email, String code) {
		if (memberRepository.existsByEmail(email)) {
			throw new PhotorizeException(ErrorType.DUPLICATE_EMAIL);
		}

		redisAuthCodeAdapter.saveOrUpdate(generateSaveKey(email), code, 5);
	}

	@Override
	public boolean isProcessingEmail(String email) {
		String key = generateSaveKey(email);
		return redisAuthCodeAdapter.hasKey(key) && redisAuthCodeAdapter.getExpireTime(key) > 0;
	}

	// 입력받은 인증코드의 유효성 검증
	@Override
	public boolean validAuthCode(String email, String code) {
		String getCode = redisAuthCodeAdapter.getValue(generateSaveKey(email))
			.orElseThrow(() -> new PhotorizeException(ErrorType.EXPIRED_EMAIL_CODE));
		return getCode.equals(code);
	}

	private String generateSaveKey(String email) {
		return StringFormat.EMAIL_AUTH_PREFIX + email;
	}

	// 인증된 이메일을 유효한 상태로 표시
	// Redis에 해당 이메일을 23시간동안 유효하다고 저장
	@Override
	public void pushAvailableEmail(String email) {
		redisAuthCodeAdapter.saveOrUpdate(generateSaveKey(email), String.valueOf(true), 1400);
	}

	// 이메일이 유효한 상태인지 확인
	@Override
	public void checkAvailableEmail(String email) {
		redisAuthCodeAdapter.getValue(generateAvailableKey(email))
			.orElseThrow(() -> new PhotorizeException(ErrorType.INVALID_EMAIL_VERIFIED));
	}

	private String generateAvailableKey(String email) {
		return StringFormat.EMAIL_AVAILABLE_PREFIX + email;
	}
}
