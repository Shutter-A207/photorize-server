package com.shutter.photorize.domain.alarm.dto.request;

import com.shutter.photorize.domain.alarm.entity.FCMToken;
import com.shutter.photorize.domain.member.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMTokenRequest {

	private String token;

	public FCMToken from(Member member) {
		return FCMToken.builder()
			.member(member)
			.token(this.token)
			.build();
	}
}
