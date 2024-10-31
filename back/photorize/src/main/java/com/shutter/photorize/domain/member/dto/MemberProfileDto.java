package com.shutter.photorize.domain.member.dto;

import com.shutter.photorize.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileDto {
	private Long memberId;
	private String nickname;
	private String img;

	@Builder
	private MemberProfileDto(Long memberId, String nickname, String img) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.img = img;
	}

	public static MemberProfileDto from(Member member) {
		return MemberProfileDto.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.img(member.getImg())
			.build();
	}
}
