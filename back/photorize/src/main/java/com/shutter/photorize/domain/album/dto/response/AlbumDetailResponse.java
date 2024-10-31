package com.shutter.photorize.domain.album.dto.response;

import java.util.List;

import com.shutter.photorize.domain.member.dto.MemberProfileDto;
import com.shutter.photorize.domain.memory.dto.MemoryInfoDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumDetailResponse {
	private String name;
	private List<MemberProfileDto> members;
	private List<MemoryInfoDto> memories;

	@Builder
	private AlbumDetailResponse(String name, List<MemberProfileDto> members, List<MemoryInfoDto> memories) {
		this.name = name;
		this.members = members;
		this.memories = memories;
	}

	public static AlbumDetailResponse of(String name, List<MemberProfileDto> members, List<MemoryInfoDto> memories) {
		return new AlbumDetailResponse(name, members, memories);
	}

}
