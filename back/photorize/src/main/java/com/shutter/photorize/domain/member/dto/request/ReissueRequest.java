package com.shutter.photorize.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequest {

	@NotBlank
	private String refreshToken;
}
