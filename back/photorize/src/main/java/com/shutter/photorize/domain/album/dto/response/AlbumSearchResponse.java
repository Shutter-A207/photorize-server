package com.shutter.photorize.domain.album.dto.response;

import java.util.List;

import com.shutter.photorize.domain.album.entity.Album;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumSearchResponse {
	private Long albumId;
	private String name;
	private List<String> memberList;

	@Builder
	private AlbumSearchResponse(Long albumId, String name, List<String> memberList) {
		this.albumId = albumId;
		this.name = name;
		this.memberList = memberList;
	}

	public static AlbumSearchResponse of(Album album, List<String> memberNicknames) {
		return AlbumSearchResponse.builder()
			.albumId(album.getId())
			.name(album.getName())
			.memberList(memberNicknames)
			.build();
	}
}
