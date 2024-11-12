package com.shutter.photorize.domain.member.dto;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KaKaoUserInfo {

	// 사용자 계정 정보를 담고 있는 객체의 키
	public static final String KAKAO_ACCOUNT = "kakao_account";
	public static final String EMAIL = "email";

	private Map<String, Object> attributes;

	// 카카오에서 받아온 사용자 정보를 Map으로 저장
	public KaKaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	// 카카오 로그인 후 받아온 사용자 정보에서 이메일 추출
	public String getEmail() {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<String, Object>> typeReferencer = new TypeReference<Map<String, Object>>() {
		};

		Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
		Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);

		return (String)account.get(EMAIL);

	}
}
