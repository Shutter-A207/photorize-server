package com.shutter.photorize.domain.member.service;

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

	public boolean createEmailAuthCode(String email, AuthCodeType authCodeType) {
		String code = authCodeService.createAuthCode(email, authCodeType);

		// log.info("Auth code={}", code);

		EmailForm emailForm = authCodeService.getAuthEmailForm(email, code, authCodeType);

		mailService.sendEmail(emailForm.getTo(), emailForm.getSubject(), emailForm.getContent(), emailForm.isHtml());

		return true;
	}

	public boolean validAuthCode(EmailAuthRequest emailAuthRequest, AuthCodeType authCodeType) {
		return authCodeService.checkValidCode(emailAuthRequest, authCodeType);
	}
}
