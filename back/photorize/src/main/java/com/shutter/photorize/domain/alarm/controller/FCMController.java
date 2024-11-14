package com.shutter.photorize.domain.alarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shutter.photorize.domain.alarm.dto.request.FCMTokenRequest;
import com.shutter.photorize.domain.alarm.service.FCMService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {

	private final FCMService fcmService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveToken(@AuthUser ContextMember contextMember,
		@RequestBody FCMTokenRequest fcmTokenRequest) {
		fcmService.saveToken(contextMember.getId(), fcmTokenRequest);
		return ApiResponse.created();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteToken(@AuthUser ContextMember contextMember,
		@RequestBody FCMTokenRequest fcmTokenRequest) {
		fcmService.deleteToken(contextMember.getId(), fcmTokenRequest);
		return ApiResponse.ok(null);
	}
}
