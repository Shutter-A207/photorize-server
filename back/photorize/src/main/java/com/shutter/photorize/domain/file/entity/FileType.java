package com.shutter.photorize.domain.file.entity;

import java.util.Arrays;
import java.util.Set;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
	PHOTO(Set.of("jpg", "jpeg", "png", "bmp", "webp", "heif", "heic", "tiff", "tif")),
	VIDEO(Set.of("mp4", "mov", "mpeg-4", "avi", "mkv", "webm", "wmv", "flv"));

	private final Set<String> extensions;

	public static FileType fromExtension(String extension) {
		return Arrays.stream(FileType.values())
			.filter(fileType -> fileType.extensions.contains(extension.toLowerCase()))
			.findFirst()
			.orElseThrow(() -> new PhotorizeException(ErrorType.FILE_UNSUPPORTED_EXTENSION));
	}
}
