package com.shutter.photorize.global.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.file.entity.S3Folder;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Utils {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${spring.cloud.aws.region.static}")
	private String region;

	@Value("${spring.cloud.aws.base-url}")
	private String s3Url;

	@Value("${spring.cloud.s3.presigned-url.expiration-minutes}")
	private long expirationMinutes;

	public String uploadProFile(MultipartFile file, S3Folder s3Folder) {
		String s3Key = generateS3Key(s3Folder);
		uploadToS3(file, s3Key);

		return String.format(s3Url, bucket, region, s3Key);
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
	public CompletableFuture<String> uploadToS3(MultipartFile file, String s3Key) {
		try {
			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucket)
					.key(s3Key)
					.contentType(file.getContentType())
					.build(),
				RequestBody.fromBytes(file.getBytes())
			);

			return CompletableFuture.completedFuture(s3Key);
		} catch (IOException e) {
			throw new PhotorizeException(ErrorType.FILE_UPLOAD_ERROR);
		}
	}

	public String generateS3Key(S3Folder s3Folder) {
		return s3Folder.getFolderPath() + "/" + UUID.randomUUID();
	}

	public String generatePreSignedUrl(String s3Key) {
		GetObjectRequest getObjectRequest = createGetObjectRequest(s3Key);
		return createPreSignedUrl(getObjectRequest);
	}

	private GetObjectRequest createGetObjectRequest(String s3Key) {
		return GetObjectRequest.builder()
			.bucket(bucket)
			.key(s3Key)
			.build();
	}

	private String createPreSignedUrl(GetObjectRequest getObjectRequest) {
		GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.getObjectRequest(getObjectRequest)
			.signatureDuration(Duration.ofMinutes(expirationMinutes))
			.build();

		return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
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
