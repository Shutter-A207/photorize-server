package com.shutter.photorize.domain.album.dto.response;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumListResponse {
	private Long albumId;
	private String name;
	private AlbumType type;
	private String img;

	@Builder
	public AlbumListResponse(Album album) {
		this.albumId = album.getId();
		this.name = album.getName();
		this.type = album.getType();
		this.img = album.getImg();
	}
}
