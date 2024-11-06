package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {
	private String email;
	private String nickname;
	private String password;
	private String passwordCheck;

	public void valid() {
		if (!password.equals(passwordCheck)) {
			throw new PhotorizeException(ErrorType.MISMATCH_PASSWORD);
		}
	}

	public Member toMember(String password, String img) {
		return Member.builder()
			.email(this.email)
			.nickname(this.nickname)
			.password(password)
			.img(img)
			.build();
	}
}
