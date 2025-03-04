package com.shutter.photorize.domain.memory.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shutter.photorize.domain.memory.entity.Memory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryInfoDto {
	private Long memoryId;
	private String url;
	private String spotName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date;

	@Builder
	private MemoryInfoDto(Long memoryId, String url, String spotName, LocalDateTime date) {
		this.memoryId = memoryId;
		this.url = url;
		this.spotName = spotName;
		this.date = date;
	}

	public static MemoryInfoDto of(Memory memory, String url, String spotName) {
		return MemoryInfoDto.builder()
			.memoryId(memory.getId())
			.url(url)
			.spotName(spotName)
			.date(memory.getDate())
			.build();
	}
}
