package com.shutter.photorize.infra.mail.service;

import com.shutter.photorize.domain.member.strategy.AuthCodeType;
import com.shutter.photorize.global.util.ResourceUtil;
import com.shutter.photorize.infra.mail.model.AuthEmailFormData;
import com.shutter.photorize.infra.mail.model.EmailForm;
import com.shutter.photorize.infra.mail.model.EmailFormData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthCodeEmailFormFactory implements EmailFormFactory {

	private final ResourceUtil resourceUtil;

	// 이메일 양식 생성
	@Override
	public EmailForm createEmailForm(String to, boolean isHtml, EmailFormData emailFormData) {
		if (!(emailFormData instanceof AuthEmailFormData)) {
			throw new IllegalArgumentException("Invalid data type");
		}

		var d = (AuthEmailFormData)emailFormData;
		String body = getMailForm().replace("{{authCode}}", d.getAuthCode())
			.replace("{{type}}", d.getAuthCodeType().getEmailFormType().getPurpose());
		String subject = getSubject(d.getAuthCodeType());
		return EmailForm.of(to, subject, body, isHtml);
	}

	// 이메일 제목 생성
	private String getSubject(AuthCodeType authCodeType) {
		return authCodeType.getEmailFormType().getPurpose() + "이메일 입니다.";
	}

	// 이메일 탬플릿 읽어오기
	private String getMailForm() {
		return resourceUtil.getHtml("classpath:templates/auth_email.html");
	}
}
