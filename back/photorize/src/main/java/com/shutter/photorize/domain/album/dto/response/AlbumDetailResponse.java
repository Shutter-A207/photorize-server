package com.shutter.photorize.domain.album.dto.response;

import java.util.List;

import com.shutter.photorize.domain.member.dto.AlbumMemberProfileDto;
import com.shutter.photorize.domain.memory.dto.MemoryInfoDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumDetailResponse {

	private Long albumId;
	private String name;
	private List<AlbumMemberProfileDto> members;
	private List<MemoryInfoDto> memories;

	@Builder
	private AlbumDetailResponse(Long albumId, String name, List<AlbumMemberProfileDto> members,
		List<MemoryInfoDto> memories) {
		this.albumId = albumId;
		this.name = name;
		this.members = members;
		this.memories = memories;
	}

	public static AlbumDetailResponse of(Long albumId, String name, List<AlbumMemberProfileDto> members,
		List<MemoryInfoDto> memories) {
		return new AlbumDetailResponse(albumId, name, members, memories);
	}

}
