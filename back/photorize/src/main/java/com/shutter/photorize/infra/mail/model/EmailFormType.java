package com.shutter.photorize.infra.mail.model;

import lombok.Getter;

@Getter
public enum EmailFormType {
	SIGNUP_AUTH("회원가입"),
	PASSWORD_CHANGE_AUTH("비밀번호 변경");

	private final String purpose;

	EmailFormType(String purpose) {
		this.purpose = purpose;
	}

}
