package com.shutter.photorize.domain.member.dto.request;

import com.shutter.photorize.domain.member.strategy.AuthCodeType;

import lombok.Getter;

@Getter
public class CodeCreateRequest {
	private String email;
	private AuthCodeType authType;
}
