package com.shutter.photorize.domain.pose.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.pose.dto.response.PoseResponse;
import com.shutter.photorize.domain.pose.service.PoseService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/poses")
@RequiredArgsConstructor
public class PoseController {

	private final PoseService poseService;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<PoseResponse>>> getAllPoses(
		@AuthUser ContextMember contextMember,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<PoseResponse> poses = poseService.getAllPoses(contextMember.getId(), pageable);
		return ApiResponse.ok(poses);
	}

	@PostMapping("/{poseId}/like")
	public ResponseEntity<ApiResponse<Void>> likePose(@PathVariable Long poseId,
		@AuthUser ContextMember contextMember) {
		poseService.likePose(poseId, contextMember.getId());
		return ApiResponse.created();
	}

	@DeleteMapping("/{poseId}/like")
	public ResponseEntity<ApiResponse<Void>> unlikePose(@PathVariable Long poseId,
		@AuthUser ContextMember contextMember) {
		poseService.unlikePose(poseId, contextMember.getId());
		return ApiResponse.ok(null);
	}
}
