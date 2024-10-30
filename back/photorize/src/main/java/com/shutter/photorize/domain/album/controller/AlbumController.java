package com.shutter.photorize.domain.album.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.dto.request.AlbumCreateRequest;
import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/albums")
public class AlbumController {

	private final AlbumService albumService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createAlbum(
		@RequestPart AlbumCreateRequest albumCreateRequest,
		@RequestPart(required = false) MultipartFile albumImage) {
		albumService.createPublicAlbum(albumCreateRequest, albumImage, "member1@example.com");
		return ApiResponse.created();
	}
}
