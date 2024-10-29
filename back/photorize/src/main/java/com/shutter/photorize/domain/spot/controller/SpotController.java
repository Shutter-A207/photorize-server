package com.shutter.photorize.domain.spot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.entity.SpotCode;
import com.shutter.photorize.domain.spot.service.SpotService;

@RestController
@RequestMapping("/api/v1/spots")
public class SpotController {

	private final SpotService spotService;

	public SpotController(SpotService spotService) {
		this.spotService = spotService;
	}

	@GetMapping
	public ResponseEntity<List<Spot>> getAllSpots() {
		List<Spot> spots = spotService.getAllSpots();
		return ResponseEntity.ok(spots);
	}

	@GetMapping("/{spotId}")
	public ResponseEntity<Spot> getSpotById(@PathVariable Long spotId) {
		Spot spot = spotService.getSpotById(spotId);
		return ResponseEntity.ok(spot);
	}

	@GetMapping("/codes")
	public ResponseEntity<List<SpotCode>> getAllSpotCodes() {
		List<SpotCode> spotCodes = spotService.getAllSpotCodes();
		return ResponseEntity.ok(spotCodes);
	}

	@GetMapping("/boundary")
	public ResponseEntity<List<Spot>> getSpotsWithinBoundary(@RequestParam Double topLeftLat,
		@RequestParam Double topLeftLng,
		@RequestParam Double botRightLat,
		@RequestParam Double botRightLng) {
		List<Spot> spots = spotService.getSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng);
		return ResponseEntity.ok(spots);
	}

	@GetMapping("/{spotId}/files")
	public ResponseEntity<List<Object>> getFilesBySpot(@PathVariable Long spotId) {
		List<Object> files = spotService.getFilesBySpot(spotId);
		return ResponseEntity.ok(files);
	}
}
