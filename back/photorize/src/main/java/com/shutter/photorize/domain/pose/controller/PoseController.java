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

import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.domain.pose.service.PoseService;

@RestController
@RequestMapping("/api/v1/poses")
public class PoseController {

	private final PoseService poseService;

	public PoseController(PoseService poseService) {
		this.poseService = poseService;
	}

	@GetMapping
	public ResponseEntity<List<Pose>> getAllPoses() {
		List<Pose> poses = poseService.getAllPoses();
		return ResponseEntity.ok(poses);
	}

	@PostMapping("/{poseId}/like")
	public ResponseEntity<Void> likePose(@PathVariable Long poseId, @RequestParam Long memberId) {
		poseService.likePose(poseId, memberId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{poseId}/like")
	public ResponseEntity<Void> unlikePose(@PathVariable Long poseId, @RequestParam Long memberId) {
		poseService.unlikePose(poseId, memberId);
		return ResponseEntity.ok().build();
	}
}
