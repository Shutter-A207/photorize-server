package com.shutter.photorize.global.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.file.entity.FileType;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Utils {

	private final S3Client s3Client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${spring.cloud.aws.region.static}")
	private String region;

	@Value("${spring.cloud.aws.base-url}")
	private String s3Url;

	public String uploadFile(MultipartFile file, FileType type) {
		String fileName = generateFileName(file, type);

		uploadToS3(file, fileName);

		return String.format(s3Url, bucket, region, fileName);
	}

	public void deleteFile(String fileUrl) {
		String fileName = extractFileName(fileUrl);
		deleteFromS3(fileName);
	}

	private void deleteFromS3(String fileName) {
		System.out.println(fileName);
		s3Client.deleteObject(DeleteObjectRequest.builder()
			.bucket(bucket)
			.key(fileName)
			.build());
	}

	@Async("fileUploadExecutor")
	public CompletableFuture<String> uploadToS3(MultipartFile file, String fileName) {
		try {
			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucket)
					.key(fileName)
					.contentType(file.getContentType())
					.build(),
				RequestBody.fromBytes(file.getBytes())
			);

			String url = String.format(s3Url, bucket, region, fileName);
			return CompletableFuture.completedFuture(url);
		} catch (IOException e) {
			throw new PhotorizeException(ErrorType.FILE_UPLOAD_ERROR);
		}
	}

	private String generateFileName(MultipartFile file, FileType type) {
		String folder = type.name().toLowerCase();
		return folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
	}

	private String extractFileName(String fileUrl) {
		try {
			URI uri = new URI(fileUrl);
			String path = uri.getPath();
			return path.startsWith("/") ? path.substring(1) : path;
		} catch (URISyntaxException e) {
			throw new PhotorizeException(ErrorType.INVALID_S3_URL);
		}
	}

}
