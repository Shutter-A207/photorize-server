package com.shutter.photorize.domain.member.entity;

public enum ProviderType {
	BASIC("basic"),
	KAKAO("kakao");

	private final String type;

	ProviderType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

}
