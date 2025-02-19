package com.shutter.photorize.domain.file.entity;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3Folder {
	PROFILE("photo/profile"),
	MEMORY("photo/memory"),
	VIDEO("video"),
	POZE("poze");

	private final String folderPath;

	public static S3Folder from(FileType fileType) {
		if (fileType == FileType.PHOTO) {
			return S3Folder.MEMORY;
		} else if (fileType == FileType.VIDEO) {
			return S3Folder.VIDEO;
		}
		throw new PhotorizeException(ErrorType.FILE_UNSUPPORTED_EXTENSION);
	}
}
