package com.shutter.photorize.domain.member.dto.response;

import java.util.Map;

import com.shutter.photorize.domain.member.entity.ProviderType;

public class KakaoResponse implements OAuth2Response {

	private final String email;

	public KakaoResponse(Map<String, Object> attribute) {
		Map<String, Object> accountInfo = (Map<String, Object>)attribute.get("kakao_account");
		this.email = accountInfo.get("email").toString();
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.KAKAO;
	}

	@Override
	public String getEmail() {
		return email;
	}
}
