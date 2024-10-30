package com.shutter.photorize.domain.file.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileService {

	private final S3Client s3Client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadFile(MultipartFile file) {
		String fileName = generateFileName(file);

		uploadToS3(file, fileName, "image/png");

		return String.format("https://%s.s3.amazonaws.com/%s", bucket, fileName);
	}

	private String generateFileName(MultipartFile file) {
		return UUID.randomUUID() + "_" + file.getOriginalFilename();
	}

	private void uploadToS3(MultipartFile file, String fileName, String contentType) {
		try {
			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucket)
					.key(fileName)
					.contentType(contentType)
					.build(),
				RequestBody.fromBytes(file.getBytes())
			);
		} catch (IOException e) {
			throw new PhotorizeException(ErrorType.FILE_UPLOAD_ERROR);
		}
	}
}
