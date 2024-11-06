package com.shutter.photorize.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.service.MemberService;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final AlbumService albumService;

	@PostMapping("/join")
	public ResponseEntity<ApiResponse<Boolean>> createMember(@RequestBody JoinRequest joinRequest) {
		Long memberId = memberService.createMember(joinRequest);
		albumService.createPrivateAlbum(memberId);

		return ApiResponse.created();
	}
}
