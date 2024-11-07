package com.shutter.photorize.domain.spot.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shutter.photorize.domain.file.entity.File;

import lombok.Getter;

@Getter
public class SpotFileResponse {

	private Long fileId;
	private Long memoryId;
	private String fileUrl;
	private String spotName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date;

	private SpotFileResponse(Long fileId, Long memoryId, String fileUrl, String spotName, LocalDateTime date) {
		this.fileId = fileId;
		this.memoryId = memoryId;
		this.fileUrl = fileUrl;
		this.spotName = spotName;
		this.date = date;
	}

	public static SpotFileResponse of(File file) {
		return new SpotFileResponse(
			file.getId(),
			file.getMemory().getId(),
			file.getUrl(),
			file.getMemory().getSpot().getName(),
			file.getMemory().getCreatedAt()
		);
	}
}
