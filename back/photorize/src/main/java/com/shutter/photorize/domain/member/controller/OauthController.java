package com.shutter.photorize.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class OauthController {

	@GetMapping("/kakao")
	public ResponseEntity<ApiResponse<?>> kakaoLogin(@RequestParam("code") String accessCode,
		HttpServletResponse response) {

	}
}
