package com.shutter.photorize.domain.spot.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class SpotWithFilesResponse {

	private Long spotId;
	private String spotName;
	private Double latitude;
	private Double longitude;
	private List<SpotFileResponse> files;

	private SpotWithFilesResponse(Long spotId, String spotName, Double latitude, Double longitude,
		List<SpotFileResponse> files) {
		this.spotId = spotId;
		this.spotName = spotName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.files = files;
	}

	public static SpotWithFilesResponse of(Long spotId, String spotName, Double latitude, Double longitude,
		List<SpotFileResponse> files) {
		return new SpotWithFilesResponse(spotId, spotName, latitude, longitude, files);
	}
}
