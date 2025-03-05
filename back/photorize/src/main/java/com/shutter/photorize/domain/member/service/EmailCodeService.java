package com.shutter.photorize.domain.member.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.strategy.EmailCodeStrategy;
import com.shutter.photorize.domain.member.strategy.EmailCodeType;
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
public class EmailCodeService {

	private final Map<EmailCodeType, EmailCodeStrategy> authCodeStrategyMap;
	private final Map<EmailFormType, EmailFormFactory> emailFormFactoryMap;

	// 인증 코드를 생성하고 저장
	public String createAuthCode(String email, EmailCodeType emailCodeType) {
		EmailCodeStrategy strategy = authCodeStrategyMap.get(emailCodeType);
		return strategy.saveAuthCode(email);
	}

	// 이메일 인증 폼 생성
	public EmailForm getAuthEmailForm(String email, String code, EmailCodeType emailCodeType) {
		AuthEmailFormData emailFormData = (AuthEmailFormData)EmailFormDataFactory.getEmailFormData(
			emailCodeType.getEmailFormType());
		emailFormData.setEmailCodeType(emailCodeType);
		emailFormData.setAuthCode(code);

		return emailFormFactoryMap.get(emailCodeType.getEmailFormType()).createEmailForm(email, true, emailFormData);
	}

	// 입력받은 인증 코드의 유효성 검증
	public Boolean checkValidCode(EmailAuthRequest emailAuthRequest, EmailCodeType emailCodeType) {
		EmailCodeStrategy strategy = authCodeStrategyMap.get(emailCodeType);

		boolean valid = strategy.validAuthCode(emailAuthRequest.getEmail(), emailAuthRequest.getAuthCode());

		if (!valid)
			throw new PhotorizeException(ErrorType.INVALID_EMAIL_CODE);

		strategy.pushAvailableEmail(emailAuthRequest.getEmail());
		return true;
	}

	// 이메일이 사용가능한 상태인지 확인
	public void checkAvailable(String email, EmailCodeType emailCodeType) {
		EmailCodeStrategy strategy = authCodeStrategyMap.get(emailCodeType);
		strategy.checkAvailableEmail(email);
	}

	// 5분 이내에 이메일 인증 코드를 요청했는지 확인
	public void checkProcessingEmail(String email, EmailCodeType emailCodeType) {
		if (isProcessingEmail(email, emailCodeType)) {
			throw new PhotorizeException(ErrorType.EMAIL_IN_PROGRESS);
		}
	}

	private boolean isProcessingEmail(String email, EmailCodeType emailCodeType) {
		EmailCodeStrategy strategy = authCodeStrategyMap.get(emailCodeType);
		return strategy.isProcessingEmail(email);
	}

}
