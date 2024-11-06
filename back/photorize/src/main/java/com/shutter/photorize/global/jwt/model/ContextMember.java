package com.shutter.photorize.global.jwt.model;

import lombok.Getter;

@Getter
public class ContextMember {
	private Long id;
	private String nickname;
	private String email;
	private String password;

	public ContextMember(Long id, String nickname, String email, String password) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
	}
}
