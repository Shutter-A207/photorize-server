package com.shutter.photorize.domain.album.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumModifyRequest {
	private String name;
	private Long colorId;
}
