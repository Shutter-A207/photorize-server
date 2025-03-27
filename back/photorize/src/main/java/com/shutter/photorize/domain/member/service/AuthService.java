package com.shutter.photorize.domain.member.service;

import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.strategy.EmailCodeType;
import com.shutter.photorize.global.jwt.service.TokenService;
import com.shutter.photorize.infra.mail.model.EmailForm;
import com.shutter.photorize.infra.mail.service.MailService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final EmailCodeService emailCodeService;
	private final MailService mailService;
	private final TokenService tokenService;
	private final MemberService memberService;

	public void reissueToken(String refreshToken, HttpServletResponse response) {
		tokenService.reissueToken(refreshToken, response);
	}

	public void reissueOAuthToken(String refreshToken, HttpServletResponse response) {
		tokenService.reissueOAuthToken(refreshToken, response);
	}

	public void createEmailAuthCode(String email, EmailCodeType emailCodeType) {
		try {
			memberService.validateDuplicateEmail(email);
			emailCodeService.checkProcessingEmail(email,
				emailCodeType);
			String code = emailCodeService.createAuthCode(email, emailCodeType);
			EmailForm emailForm = emailCodeService.getAuthEmailForm(email, code, emailCodeType);
			mailService.sendEmail(emailForm.getTo(), emailForm.getSubject(),
				emailForm.getContent(), emailForm.isHtml());
		} catch (Exception e) {
			log.error("Failed to send authentication email to: {}", email, e);
		}
	}

	public boolean validAuthCode(EmailAuthRequest emailAuthRequest, EmailCodeType emailCodeType) {
		return emailCodeService.checkValidCode(emailAuthRequest, emailCodeType);
	}
}
