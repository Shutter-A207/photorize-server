package com.shutter.photorize.domain.spot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.spot.dto.response.SpotResponse;
import com.shutter.photorize.domain.spot.dto.response.SpotWithFilesResponse;
import com.shutter.photorize.domain.spot.service.SpotService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/spots")
@RequiredArgsConstructor
public class SpotController {

	private final SpotService spotService;

	@GetMapping("/boundary")
	public ResponseEntity<ApiResponse<List<SpotResponse>>> getSpotsWithinBoundary(
		@RequestParam Double topLeftLat,
		@RequestParam Double topLeftLng,
		@RequestParam Double botRightLat,
		@RequestParam Double botRightLng,
		@AuthUser ContextMember contextMember) {

		List<SpotResponse> spots = spotService.getSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng,
			contextMember.getId());
		return ApiResponse.ok(spots);
	}

	@GetMapping("/{spotId}/memories")
	public ResponseEntity<ApiResponse<SpotWithFilesResponse>> getFilesBySpot(
		@PathVariable Long spotId, @AuthUser ContextMember contextMember) {

		SpotWithFilesResponse response = spotService.getFilesBySpot(spotId, contextMember.getId());
		return ApiResponse.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<SpotResponse>>> getAllSpots(@AuthUser ContextMember contextMember) {
		List<SpotResponse> spots = spotService.getAllSpots(contextMember.getId());
		return ApiResponse.ok(spots);
	}

}
