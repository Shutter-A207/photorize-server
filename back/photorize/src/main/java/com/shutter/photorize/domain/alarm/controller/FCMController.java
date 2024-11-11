package com.shutter.photorize.domain.alarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shutter.photorize.domain.alarm.dto.request.FCMTokenSaveRequest;
import com.shutter.photorize.domain.alarm.service.FCMService;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {

	private final FCMService fcmService;

	public ResponseEntity<ApiResponse<Void>> saveToken(@AuthUser Long memberId,
		FCMTokenSaveRequest fcmTokenSaveRequest) {
		fcmService.saveToken(memberId, fcmTokenSaveRequest);
		return ApiResponse.created();
	}
}
