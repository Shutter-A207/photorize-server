package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String passwordCheck;

	public void valid() {
		if (!password.equals(passwordCheck)) {
			throw new PhotorizeException(ErrorType.MISMATCH_PASSWORD);
		}
	}
}
