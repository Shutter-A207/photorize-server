package com.shutter.photorize.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SigninRequest {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&()_\\-+=])[A-Za-z\\d!@#$%^&()_\\-+=]{8,20}$",
		message = "유효하지 않은 비밀번호입니다!"
	)
	private String password;
}
