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
	private boolean status;

	@Builder
	private MemberProfileDto(Long memberId, String nickname, String img, boolean status) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.img = img;
		this.status = status;
	}

	public static MemberProfileDto from(Member member, boolean stauts) {
		return MemberProfileDto.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.img(member.getImg())
			.status(stauts)
			.build();
	}
}
