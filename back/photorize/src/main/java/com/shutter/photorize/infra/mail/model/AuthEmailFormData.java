package com.shutter.photorize.infra.mail.model;

import com.shutter.photorize.domain.member.strategy.AuthCodeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthEmailFormData implements EmailFormData {
	private AuthCodeType authCodeType;
	private String authCode;

	public AuthEmailFormData() {
	}

	public AuthEmailFormData(AuthCodeType authCodeType, String authCode) {
		this.authCodeType = authCodeType;
		this.authCode = authCode;
	}
}
