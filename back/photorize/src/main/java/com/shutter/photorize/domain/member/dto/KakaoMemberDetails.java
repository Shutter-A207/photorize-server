package com.shutter.photorize.domain.member.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoMemberDetails implements OAuth2User {

	private final String email;
	private final List<? extends GrantedAuthority> authorities;
	private final Map<String, Object> attributes;

	@Override
	public String getName() {
		return email;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
