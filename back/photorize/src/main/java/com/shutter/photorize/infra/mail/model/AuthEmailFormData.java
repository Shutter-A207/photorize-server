package com.shutter.photorize.infra.mail.model;

import com.shutter.photorize.domain.member.strategy.EmailCodeType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthEmailFormData implements EmailFormData {
	private EmailCodeType emailCodeType;
	private String authCode;

	public AuthEmailFormData() {
	}

	public AuthEmailFormData(EmailCodeType emailCodeType, String authCode) {
		this.emailCodeType = emailCodeType;
		this.authCode = authCode;
	}
}
