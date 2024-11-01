package com.shutter.photorize.domain.album.entity;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlbumType {

	PRIVATE("PRIVATE"), PUBLIC("PUBLIC");

	private final String value;

	public static AlbumType convertToEunm(String type) {
		AlbumType albumType;

		try {
			albumType = AlbumType.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new PhotorizeException(ErrorType.NO_ALBUM_TYPE_FOUND);
		}

		return albumType;
	}
}
