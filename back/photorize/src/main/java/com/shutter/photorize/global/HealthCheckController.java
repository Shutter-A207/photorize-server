package com.shutter.photorize.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {

	@GetMapping("/health")
	public ResponseEntity<?> healthCheck() {
		return ResponseEntity.ok("ok");
	}
}
