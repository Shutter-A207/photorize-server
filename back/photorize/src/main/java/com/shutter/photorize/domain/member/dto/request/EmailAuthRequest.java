package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.strategy.EmailCodeType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailAuthRequest {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String authCode;

	@NotNull
	private EmailCodeType authType;
}
