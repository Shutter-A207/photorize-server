package com.shutter.photorize.domain.spot.entity;

import com.shutter.photorize.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Spot extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_code_id", nullable = false)
	private SpotCode spotCode;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private String name;

	private Spot(SpotCode spotCode, Double latitude, Double longitude, String name) {
		this.spotCode = spotCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}

	public static Spot create(SpotCode spotCode, Double latitude, Double longitude, String name) {
		return new Spot(spotCode, latitude, longitude, name);
	}
}
