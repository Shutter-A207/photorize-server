package com.shutter.photorize.domain.memory.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shutter.photorize.domain.memory.entity.Memory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainMemoryResponse {
	private Long memoryId;
	private String url;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date;
	private Long albumId;
	private String albumName;

	@Builder
	private MainMemoryResponse(Long memoryId, String url, LocalDateTime date, Long albumId, String albumName) {
		this.memoryId = memoryId;
		this.url = url;
		this.date = date;
		this.albumId = albumId;
		this.albumName = albumName;
	}

	public static MainMemoryResponse of(Memory memory, String url) {
		return MainMemoryResponse.builder()
			.memoryId(memory.getId())
			.url(url)
			.date(memory.getDate())
			.albumId(memory.getAlbum().getId())
			.albumName(memory.getAlbum().getName())
			.build();
	}
}
