package com.shutter.photorize.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.domain.member.dto.request.ChangePasswordRequest;
import com.shutter.photorize.domain.member.dto.request.CodeCreateRequest;
import com.shutter.photorize.domain.member.dto.request.EmailAuthRequest;
import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.service.AuthService;
import com.shutter.photorize.domain.member.service.MemberService;
import com.shutter.photorize.domain.member.strategy.AuthCodeType;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final AlbumService albumService;
	private final AuthService authService;

	@PostMapping("/join")
	public ResponseEntity<ApiResponse<Boolean>> createMember(@RequestBody JoinRequest joinRequest) {
		Long memberId = memberService.createMember(joinRequest);
		albumService.createPrivateAlbum(memberId);

		return ApiResponse.created();
	}

	@PostMapping("/email/code")
	public ResponseEntity<ApiResponse<Boolean>> createEmailAuthCode(@RequestBody CodeCreateRequest codeCreateRequest) {

		boolean result = authService.createEmailAuthCode(codeCreateRequest.getEmail(),
			AuthCodeType.of(String.valueOf(codeCreateRequest.getAuthType())));
		return ApiResponse.ok(result);
	}

	@PostMapping("/email/verifyCode")
	public ResponseEntity<ApiResponse<Boolean>> validEmailAuthCode(@RequestBody EmailAuthRequest emailAuthRequest) {
		boolean result = authService.validAuthCode(emailAuthRequest,
			AuthCodeType.of(String.valueOf(emailAuthRequest.getAuthType())));
		return ApiResponse.ok(result);
	}

	@PostMapping("/password")
	public ResponseEntity<ApiResponse<Boolean>> changePassword(
		@RequestBody ChangePasswordRequest changePasswordRequest) {
		boolean result = memberService.modifyPassword(changePasswordRequest);

		return ApiResponse.ok(result);
	}
}
