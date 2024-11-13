package com.shutter.photorize.domain.album.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumModifyRequest {

	@NotBlank
	@Size(min = 2, max = 12)
	private String name;

	@NotNull
	private Long colorId;
}
