package com.shutter.photorize.domain.member.dto.response;

import com.shutter.photorize.domain.member.entity.ProviderType;

public interface OAuth2Response {

	ProviderType getProviderType();

	String getEmail();
}
