package com.shutter.photorize.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListDto {
	private Long memberId;
	private String nickname;
	private Long privateAlbumId;

	@Builder
	private MemberListDto(Long memberId, String nickname, Long privateAlbumId) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.privateAlbumId = privateAlbumId;
	}

	public static MemberListDto from(Long memberId, String nickname, Long privateAlbumId) {
		return new MemberListDto(memberId, nickname, privateAlbumId);
	}
}
