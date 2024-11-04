package com.shutter.photorize.domain.album.dto.request;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumModifyRequest {
	@Nullable
	private String name;
	@Nullable
	private Long colorId;
}
