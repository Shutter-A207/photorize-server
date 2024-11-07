package com.shutter.photorize.domain.spot.dto.response;

import com.shutter.photorize.domain.file.entity.File;

import lombok.Getter;

@Getter
public class SpotFileResponse {

	private Long fileId;
	private Long memoryId;
	private String fileUrl;
	private String fileType;

	private SpotFileResponse(Long fileId, Long memoryId, String fileUrl, String fileType) {
		this.fileId = fileId;
		this.memoryId = memoryId;
		this.fileUrl = fileUrl;
		this.fileType = fileType;
	}

	public static SpotFileResponse of(File file) {
		return new SpotFileResponse(
			file.getId(),
			file.getMemory().getId(),
			file.getUrl(),
			file.getType().name()
		);
	}
}
