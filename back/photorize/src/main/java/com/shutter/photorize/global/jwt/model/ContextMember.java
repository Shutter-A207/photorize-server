package com.shutter.photorize.global.jwt.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContextMember {
	private Long id;
	private String nickname;
	private String email;
	private String password;

	@Builder
	private ContextMember(Long id, String nickname, String email, String password) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
	}

	public static ContextMember of(Long id, String nickname, String email, String password) {
		return new ContextMember(id, nickname, email, password);
	}
}
