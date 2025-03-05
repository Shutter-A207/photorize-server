package com.shutter.photorize.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.member.dto.request.ChangePasswordRequest;
import com.shutter.photorize.domain.member.dto.request.CodeCreateRequest;
import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.dto.request.ReissueRequest;
import com.shutter.photorize.domain.member.service.AuthService;
import com.shutter.photorize.domain.member.service.EmailCodeService;
import com.shutter.photorize.domain.member.service.MemberService;
import com.shutter.photorize.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final AuthService authService;
	private final EmailCodeService emailCodeService;

	@PostMapping("/join")
	public ResponseEntity<ApiResponse<Boolean>> createMember(@RequestBody @Valid JoinRequest joinRequest) {
		memberService.createMember(joinRequest);
		return ApiResponse.created();
	}

	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<Boolean>> reissue(@RequestBody @Valid ReissueRequest reissue,
		HttpServletResponse response) {
		authService.reissueToken(reissue.getRefreshToken(), response);
		return ApiResponse.ok(true);
	}

	@PostMapping("/oauth/reissue")
	public ResponseEntity<ApiResponse<Boolean>> reissueOAuth(
		@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response) {
		authService.reissueOAuthToken(refreshToken, response);
		return ApiResponse.ok(true);
	}

	@PostMapping("/email/code")
	public ResponseEntity<ApiResponse<Boolean>> createEmailAuthCode(
		@RequestBody @Valid CodeCreateRequest codeCreateRequest) {
		log.info("Executing createEmailAuthCode in thread: {}", Thread.currentThread().getName());
		memberService.validateDuplicateEmail(codeCreateRequest.getEmail());
		emailCodeService.checkProcessingEmail(codeCreateRequest.getEmail(),
			codeCreateRequest.getAuthType());
		authService.createEmailAuthCode(codeCreateRequest.getEmail(),
			codeCreateRequest.getAuthType());
		return ApiResponse.ok(true);
	}

	@PostMapping("/email/verifyCode")
	public ResponseEntity<ApiResponse<Boolean>> validEmailAuthCode(
		@RequestBody @Valid EmailAuthRequest emailAuthRequest) {
		boolean result = authService.validAuthCode(emailAuthRequest,
			emailAuthRequest.getAuthType());
		return ApiResponse.ok(result);
	}

	@PostMapping("/password")
	public ResponseEntity<ApiResponse<Boolean>> changePassword(
		@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
		boolean result = memberService.modifyPassword(changePasswordRequest);

		return ApiResponse.ok(result);
	}
}
