package com.shutter.photorize.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.member.dto.LoginMemberProfile;
import com.shutter.photorize.domain.member.service.MemberService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<ApiResponse<LoginMemberProfile>> getMember(
		@AuthUser ContextMember contextMember) {
		LoginMemberProfile loginMemberProfile = memberService.getLoginMemberProfile(contextMember.getId());

		return ApiResponse.ok(loginMemberProfile);
	}
}
