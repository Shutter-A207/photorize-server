package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.strategy.EmailCodeType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CodeCreateRequest {

	@NotBlank
	@Email
	private String email;

	@NotNull
	private EmailCodeType authType;
}
