package com.shutter.photorize.domain.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.member.dto.LoginMemberProfileDto;
import com.shutter.photorize.domain.member.dto.MemberListDto;
import com.shutter.photorize.domain.member.dto.request.UpdateNicknameRequest;
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
	public ResponseEntity<ApiResponse<List<MemberListDto>>> searchMember(
		@RequestParam String keyword,
		@AuthUser ContextMember contextMember) {
		List<MemberListDto> memberListDtos = memberService.getMembers(keyword, contextMember.getId());

		return ApiResponse.ok(memberListDtos);
	}

	@PostMapping("/checkNickname")
	public ResponseEntity<ApiResponse<Boolean>> checkNickname(
		@RequestBody UpdateNicknameRequest updateNicknameRequest) {
		Boolean isPossible = memberService.validateNickname(updateNicknameRequest.getNickname());

		return ApiResponse.ok(isPossible);
	}

	@PostMapping("/updateImg")
	public ResponseEntity<ApiResponse<LoginMemberProfileDto>> updateImg(
		@AuthUser ContextMember contextMember,
		@RequestParam MultipartFile profileImage) {
		LoginMemberProfileDto loginMemberProfileDto = memberService.updateImg(contextMember.getId(), profileImage);

		return ApiResponse.ok(loginMemberProfileDto);
	}

	@PostMapping("/updateNickname")
	public ResponseEntity<ApiResponse<LoginMemberProfileDto>> updateNickname(
		@AuthUser ContextMember contextMember,
		@RequestBody UpdateNicknameRequest updateNicknameRequest) {
		LoginMemberProfileDto loginMemberProfileDto = memberService.updateNickname(contextMember.getId(),
			updateNicknameRequest);

		return ApiResponse.ok(loginMemberProfileDto);
	}
}
