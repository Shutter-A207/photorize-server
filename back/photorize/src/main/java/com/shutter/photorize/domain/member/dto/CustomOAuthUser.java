package com.shutter.photorize.domain.member.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.shutter.photorize.domain.member.entity.ProviderType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuthUser implements OAuth2User {
	@Getter
	private Long id;
	private final String email;
	@Getter
	private final ProviderType providerType;

	@Builder
	private CustomOAuthUser(Long id, String email, ProviderType providerType) {
		this.id = id;
		this.email = email;
		this.providerType = providerType;
	}

	public static CustomOAuthUser of(Long id, String email, ProviderType providerType) {
		return new CustomOAuthUser(id, email, providerType);
	}

	@Override
	public String getName() {
		return email;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
}
