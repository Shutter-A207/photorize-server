package com.shutter.photorize.domain.file.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.file.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	//업로드 테스트
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		String imageUrl = fileService.uploadFile(file);
		return "imageUrl: " + imageUrl;
	}
}
