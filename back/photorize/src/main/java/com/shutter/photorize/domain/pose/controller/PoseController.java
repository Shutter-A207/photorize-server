package com.shutter.photorize.domain.pose.controller;

import java.util.List;

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
import com.shutter.photorize.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/poses")
@RequiredArgsConstructor
public class PoseController {

	private final PoseService poseService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<PoseResponse>>> getAllPoses(@RequestParam Long memberId) {
		List<PoseResponse> poses = poseService.getAllPoses(memberId);
		return ApiResponse.ok(poses);
	}

	@PostMapping("/{poseId}/like")
	public ResponseEntity<ApiResponse<Void>> likePose(@PathVariable Long poseId, @RequestParam Long memberId) {
		poseService.likePose(poseId, memberId);
		return ApiResponse.created();
	}

	@DeleteMapping("/{poseId}/like")
	public ResponseEntity<ApiResponse<Void>> unlikePose(@PathVariable Long poseId, @RequestParam Long memberId) {
		poseService.unlikePose(poseId, memberId);
		return ApiResponse.ok(null);
	}
}
