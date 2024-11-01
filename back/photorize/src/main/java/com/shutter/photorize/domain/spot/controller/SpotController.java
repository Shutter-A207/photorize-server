package com.shutter.photorize.domain.spot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.spot.dto.response.SpotResponse;
import com.shutter.photorize.domain.spot.service.SpotService;
import com.shutter.photorize.global.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/spots")
public class SpotController {

	private final SpotService spotService;

	public SpotController(SpotService spotService) {
		this.spotService = spotService;
	}

	@GetMapping("/boundary")
	public ResponseEntity<ApiResponse<List<SpotResponse>>> getSpotsWithinBoundary(
		@RequestParam Double topLeftLat,
		@RequestParam Double topLeftLng,
		@RequestParam Double botRightLat,
		@RequestParam Double botRightLng) {
		List<SpotResponse> spots = spotService.getSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng);
		return ApiResponse.ok(spots);
	}

	@GetMapping("/{spotId}/files")
	public ResponseEntity<ApiResponse<List<Object>>> getFilesBySpot(@PathVariable Long spotId) {
		List<Object> files = spotService.getFilesBySpot(spotId);
		return ApiResponse.ok(files);
	}
}
