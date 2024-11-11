package com.shutter.photorize.domain.alarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shutter.photorize.domain.alarm.dto.request.FCMTokenSaveRequest;
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
		@RequestBody FCMTokenSaveRequest fcmTokenSaveRequest) {
		fcmService.saveToken(contextMember.getId(), fcmTokenSaveRequest);
		return ApiResponse.created();
	}
}
