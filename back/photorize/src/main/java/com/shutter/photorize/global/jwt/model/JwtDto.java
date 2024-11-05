package com.shutter.photorize.global.jwt.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtDto {
	private String accessToken;
	private String refreshToken;

	@Builder
	private JwtDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public static JwtDto of(String accessToken, String refreshToken) {
		return new JwtDto(accessToken, refreshToken);
	}
}
