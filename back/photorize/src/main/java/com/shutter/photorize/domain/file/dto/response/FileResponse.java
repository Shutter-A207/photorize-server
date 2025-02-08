package com.shutter.photorize.domain.file.dto.response;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.entity.FileType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileResponse {

	private String url;
	private FileType type;

	@Builder
	public FileResponse(String url, FileType type) {
		this.url = url;
		this.type = type;
	}

	public static FileResponse of(File file, String url) {
		return FileResponse.builder()
			.url(url)
			.type(file.getType())
			.build();
	}
}
