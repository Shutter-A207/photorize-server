package com.shutter.photorize.domain.member.dto;

import com.shutter.photorize.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginMemberProfileDto {
	private Long memberId;
	private String nickname;
	private String img;

	@Builder
	private LoginMemberProfileDto(Long memberId, String nickname, String img) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.img = img;
	}

	public static LoginMemberProfileDto of(Member member) {
		return new LoginMemberProfileDto(member.getId(), member.getNickname(), member.getImg());
	}
}
