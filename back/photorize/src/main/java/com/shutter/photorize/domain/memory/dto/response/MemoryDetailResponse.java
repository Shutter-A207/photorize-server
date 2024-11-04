package com.shutter.photorize.domain.memory.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.shutter.photorize.domain.file.dto.response.FileResponse;
import com.shutter.photorize.domain.memory.entity.Memory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryDetailResponse {

	private Long writerId;
	private String nickname;
	private String writerImg;
	private List<FileResponse> files;
	private LocalDate date;
	private String spotName;
	private String content;

	@Builder
	private MemoryDetailResponse(Long writerId, String nickname, String writerImg, List<FileResponse> files, LocalDate date,
		String spotName, String content) {
		this.writerId = writerId;
		this.nickname = nickname;
		this.writerImg = writerImg;
		this.files = files;
		this.date = date;
		this.spotName = spotName;
		this.content = content;
	}

	public static MemoryDetailResponse of(Memory memory, List<FileResponse> files) {
		return MemoryDetailResponse.builder()
			.writerId(memory.getMember().getId())
			.nickname(memory.getMember().getNickname())
			.writerImg(memory.getMember().getImg())
			.files(files)
			.date(LocalDate.from(memory.getDate()))
			.spotName(memory.getSpot().getName())
			.content(memory.getContent())
			.build();
	}
}
