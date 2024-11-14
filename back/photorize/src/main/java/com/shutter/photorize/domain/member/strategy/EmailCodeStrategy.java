package com.shutter.photorize.domain.member.strategy;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public interface EmailCodeStrategy {

	// 이메일 인증코드 생성 및 저장
	default String saveAuthCode(String email) {
		String code = createCode();
		save(email, code);
		return code;
	}

	// 인증 코드 생성
	private String createCode() {
		int length = 6;
		try {
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			SecureRandom random = SecureRandom.getInstanceStrong();
			StringBuilder code = new StringBuilder();

			for (int i = 0; i < length; i++) {
				int index = random.nextInt(characters.length());
				code.append(characters.charAt(index));
			}

			return code.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	// 인증 코드 저장
	void save(String key, String code);

	// 인증 코드 유효한지 검증
	boolean validAuthCode(String email, String code);

	// 인증 가능한 이메일 등록
	void pushAvailableEmail(String email);

	// 해당 이메일이 인증 가능한 상태인지 확인
	void checkAvailableEmail(String email);

}
