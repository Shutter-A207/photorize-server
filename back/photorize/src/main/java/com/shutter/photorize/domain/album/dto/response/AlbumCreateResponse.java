package com.shutter.photorize.domain.album.dto.response;

import com.shutter.photorize.domain.album.entity.Album;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateResponse {
	private Long albumId;
	private String name;

	@Builder
	private AlbumCreateResponse(Long albumId, String name) {
		this.albumId = albumId;
		this.name = name;
	}

	public static AlbumCreateResponse of(Album album) {
		return new AlbumCreateResponse(album.getId(), album.getName());
	}
}
