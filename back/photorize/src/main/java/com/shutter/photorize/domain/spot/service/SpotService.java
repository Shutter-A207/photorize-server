package com.shutter.photorize.domain.spot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.spot.dto.response.SpotResponse;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.repository.SpotRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpotService {

	private final SpotRepository spotRepository;

	@Transactional(readOnly = true)
	public List<SpotResponse> getSpotsWithinBoundary(Double topLeftLat, Double topLeftLng, Double botRightLat,
		Double botRightLng) {
		return spotRepository.findSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng)
			.stream()
			.map(spot -> new SpotResponse(
				spot.getId(),
				spot.getSpotCode().getName(),
				spot.getName(),
				spot.getLatitude(),
				spot.getLongitude()
			))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<Object> getFilesBySpot(Long spotId) {
		Spot spot = spotRepository.findById(spotId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.SPOT_NOT_FOUND));
		return spotRepository.findFilesBySpotId(spotId);
	}
}
