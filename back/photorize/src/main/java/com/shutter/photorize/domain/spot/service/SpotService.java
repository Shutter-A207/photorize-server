package com.shutter.photorize.domain.spot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.entity.SpotCode;
import com.shutter.photorize.domain.spot.repository.SpotCodeRepository;
import com.shutter.photorize.domain.spot.repository.SpotRepository;

@Service
public class SpotService {

	private final SpotRepository spotRepository;
	private final SpotCodeRepository spotCodeRepository;

	public SpotService(SpotRepository spotRepository, SpotCodeRepository spotCodeRepository) {
		this.spotRepository = spotRepository;
		this.spotCodeRepository = spotCodeRepository;
	}

	@Transactional(readOnly = true)
	public List<Spot> getAllSpots() {
		return spotRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Spot getSpotById(Long spotId) {
		return spotRepository.findById(spotId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지점입니다."));
	}

	@Transactional(readOnly = true)
	public List<SpotCode> getAllSpotCodes() {
		return spotCodeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Spot> getSpotsWithinBoundary(Double topLeftLat, Double topLeftLng, Double botRightLat,
		Double botRightLng) {
		return spotRepository.findSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng);
	}

	@Transactional(readOnly = true)
	public List<Object> getFilesBySpot(Long spotId) {
		return spotRepository.findFilesBySpotId(spotId);
	}
}
