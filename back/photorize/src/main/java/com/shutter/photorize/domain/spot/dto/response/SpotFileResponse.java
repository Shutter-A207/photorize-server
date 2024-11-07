package com.shutter.photorize.domain.spot.dto.response;

import java.time.LocalDateTime;

import com.shutter.photorize.domain.file.entity.File;

import lombok.Getter;

@Getter
public class SpotFileResponse {

	private Long fileId;
	private Long memoryId;
	private String fileUrl;
	private String fileType;
	private String spotName;
	private LocalDateTime date;

	private SpotFileResponse(Long fileId, Long memoryId, String fileUrl, String fileType, String spotName,
		LocalDateTime date) {
		this.fileId = fileId;
		this.memoryId = memoryId;
		this.fileUrl = fileUrl;
		this.fileType = fileType;
		this.spotName = spotName;
		this.date = date;
	}

	public static SpotFileResponse of(File file) {
		return new SpotFileResponse(
			file.getId(),
			file.getMemory().getId(),
			file.getUrl(),
			file.getType().name(),
			file.getMemory().getSpot().getName(),
			file.getMemory().getCreatedAt()
		);
	}
}
