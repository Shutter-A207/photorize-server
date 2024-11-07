package com.shutter.photorize.domain.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.member.dto.LoginMemberProfileDto;
import com.shutter.photorize.domain.member.dto.MemberListDto;
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
	public ResponseEntity<ApiResponse<LoginMemberProfileDto>> getMember(
		@AuthUser ContextMember contextMember) {
		LoginMemberProfileDto loginMemberProfileDto = memberService.getLoginMemberProfile(contextMember.getId());

		return ApiResponse.ok(loginMemberProfileDto);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<MemberListDto>>> getAllMembers(
		@AuthUser ContextMember contextMember) {
		List<MemberListDto> memberListDtos = memberService.getAllMembers(contextMember.getId());

		return ApiResponse.ok(memberListDtos);
	}

	@GetMapping("/checkNickname")
	public ResponseEntity<ApiResponse<Boolean>> checkNickname(
		@RequestParam String nickname) {
		Boolean isPossible = memberService.validateNickname(nickname);

		return ApiResponse.ok(isPossible);
	}
}
