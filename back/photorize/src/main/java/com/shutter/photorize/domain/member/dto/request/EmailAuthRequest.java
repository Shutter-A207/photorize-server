package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.strategy.AuthCodeType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailAuthRequest {
	private String email;
	private String authCode;
	private AuthCodeType authType;
}
