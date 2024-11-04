package com.shutter.photorize.domain.pose.entity;

import com.shutter.photorize.global.entity.BaseEntity;

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
public class Pose extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PoseHeadcount headcount;

	@Column(nullable = false, unique = true)
	private String img;

	private Pose(PoseHeadcount headcount, String img) {
		this.headcount = headcount;
		this.img = img;
	}

	@Builder
	public static Pose create(PoseHeadcount headcount, String img) {
		return new Pose(headcount, img);
	}
}
