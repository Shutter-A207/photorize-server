package com.shutter.photorize.domain.member.entity;

import com.shutter.photorize.global.entity.UpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String img;

	@Builder
	private Member(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

}
