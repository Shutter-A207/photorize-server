package com.shutter.photorize.domain.member.entity;

import com.shutter.photorize.global.entity.UpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends UpdatableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = true)
	private String password;

	@Column(nullable = false)
	private String img;

	@Enumerated(EnumType.STRING)
	private ProviderType provider;

	@Builder
	private Member(String email, String nickname, String password, String img, ProviderType provider) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.img = img;
		this.provider = provider;
	}

	public static Member of(String email, String nickname, String password, String img, ProviderType provider) {
		return new Member(email, nickname, password, img, provider);
	}

	public void updateProfile(String img) {
		this.img = img;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

}
