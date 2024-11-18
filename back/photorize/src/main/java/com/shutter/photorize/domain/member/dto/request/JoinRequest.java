package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.entity.ProviderType;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 2, max = 8)
	@Pattern(
		regexp = "^[가-힣a-zA-Z0-9]*$",
		message = "닉네임은 한글과 영문자만 사용 가능합니다"
	)
	private String nickname;

	@NotBlank
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&()_\\-+=])[A-Za-z\\d!@#$%^&()_\\-+=]{8,20}$",
		message = "유효하지 않은 비밀번호입니다!"
	)
	private String password;

	@NotBlank
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&()_\\-+=])[A-Za-z\\d!@#$%^&()_\\-+=]{8,20}$",
		message = "유효하지 않은 비밀번호입니다!"
	)
	private String passwordCheck;

	public void valid() {
		if (!password.equals(passwordCheck)) {
			throw new PhotorizeException(ErrorType.MISMATCH_PASSWORD);
		}
	}

	public Member toMember(String password, String img, ProviderType providerType) {
		return Member.builder()
			.email(this.email)
			.nickname(this.nickname)
			.password(password)
			.img(img)
			.provider(providerType)
			.build();
	}
}
