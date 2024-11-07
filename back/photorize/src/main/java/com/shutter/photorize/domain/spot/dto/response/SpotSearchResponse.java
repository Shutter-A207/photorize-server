package com.shutter.photorize.domain.spot.dto.response;

import lombok.Getter;

@Getter
public class SpotSearchResponse {

	private Long spotId;
	private String spotName;

	private SpotSearchResponse(Long spotId, String spotName) {
		this.spotId = spotId;
		this.spotName = spotName;
	}

	public static SpotSearchResponse of(Long spotId, String spotName) {
		return new SpotSearchResponse(spotId, spotName);
	}
}
