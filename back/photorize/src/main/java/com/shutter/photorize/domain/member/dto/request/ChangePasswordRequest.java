package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
	@NotBlank
	@Email
	private String email;

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
}
