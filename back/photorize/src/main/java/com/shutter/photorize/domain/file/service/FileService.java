package com.shutter.photorize.domain.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.global.util.S3Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final S3Utils s3Utils;

	public String uploadFile(MultipartFile file) {
		return s3Utils.uploadFile(file);
	}
}
