package com.shutter.photorize.domain.member.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.strategy.AuthCodeStrategy;
import com.shutter.photorize.domain.member.strategy.AuthCodeType;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.infra.mail.model.AuthEmailFormData;
import com.shutter.photorize.infra.mail.model.EmailForm;
import com.shutter.photorize.infra.mail.model.EmailFormType;
import com.shutter.photorize.infra.mail.service.EmailFormDataFactory;
import com.shutter.photorize.infra.mail.service.EmailFormFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

	private final Map<AuthCodeType, AuthCodeStrategy> authCodeStrategyMap;
	private final Map<EmailFormType, EmailFormFactory> emailFormFactoryMap;

	// 인증 코드를 생성하고 저장
	public String createAuthCode(String email, AuthCodeType authCodeType) {
		AuthCodeStrategy strategy = authCodeStrategyMap.get(authCodeType);
		return strategy.saveAuthCode(email);
	}

	// 이메일 인증 폼 생성
	public EmailForm getAuthEmailForm(String email, String code, AuthCodeType authCodeType) {
		AuthEmailFormData emailFormData = (AuthEmailFormData)EmailFormDataFactory.getEmailFormData(
			authCodeType.getEmailFormType());
		emailFormData.setAuthCodeType(authCodeType);
		emailFormData.setAuthCode(code);

		return emailFormFactoryMap.get(authCodeType.getEmailFormType()).createEmailForm(email, true, emailFormData);
	}

	// 입력받은 인증 코드의 유효성 검증
	public Boolean checkValidCode(EmailAuthRequest emailAuthRequest, AuthCodeType authCodeType) {
		AuthCodeStrategy strategy = authCodeStrategyMap.get(authCodeType);

		boolean valid = strategy.validAuthCode(emailAuthRequest.getEmail(), emailAuthRequest.getAuthCode());

		if (!valid)
			throw new PhotorizeException(ErrorType.INVALID_EMAIL_CODE);

		strategy.pushAvailableEmail(emailAuthRequest.getEmail());
		return true;
	}

	// 이메일이 사용가능한 상태인지 확인
	public void checkAvailable(String email, AuthCodeType authCodeType) {
		AuthCodeStrategy strategy = authCodeStrategyMap.get(authCodeType);
		strategy.checkAvailableEmail(email);
	}

}
