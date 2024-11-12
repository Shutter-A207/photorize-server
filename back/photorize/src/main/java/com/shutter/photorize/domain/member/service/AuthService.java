package com.shutter.photorize.domain.member.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.strategy.AuthCodeType;
import com.shutter.photorize.infra.mail.model.EmailForm;
import com.shutter.photorize.infra.mail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthCodeService authCodeService;
	private final MailService mailService;

	@Async
	public void createEmailAuthCode(String email, AuthCodeType authCodeType) {
		try {
			String code = authCodeService.createAuthCode(email, authCodeType);
			EmailForm emailForm = authCodeService.getAuthEmailForm(email, code, authCodeType);
			mailService.sendEmail(emailForm.getTo(), emailForm.getSubject(),
				emailForm.getContent(), emailForm.isHtml());
		} catch (Exception e) {
			log.error("Failed to send authentication email to: {}", email, e);
		}
	}

	public boolean validAuthCode(EmailAuthRequest emailAuthRequest, AuthCodeType authCodeType) {
		return authCodeService.checkValidCode(emailAuthRequest, authCodeType);
	}
}
