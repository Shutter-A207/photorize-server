package com.shutter.photorize.domain.alarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shutter.photorize.domain.alarm.dto.request.FCMTokenSaveRequest;
import com.shutter.photorize.domain.alarm.service.FCMService;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {

	private final FCMService fcmService;
	private final AlbumRepository albumRepository;

	public ResponseEntity<ApiResponse<Void>> saveToken(@AuthUser Long memberId,
		FCMTokenSaveRequest fcmTokenSaveRequest) {
		fcmService.saveToken(memberId, fcmTokenSaveRequest);
		return ApiResponse.created();
	}

	@PostMapping("/send")
	public ResponseEntity<ApiResponse<Void>> sendMessage(@RequestBody FCMTokenSaveRequest fcmTokenSaveRequest) {
		//test
		Album album = albumRepository.getOrThrow(1L);
		fcmService.sendPublicAlarm(fcmTokenSaveRequest.getToken(), album);
		return ApiResponse.created();
	}
}
