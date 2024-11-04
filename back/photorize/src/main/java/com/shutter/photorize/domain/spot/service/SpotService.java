package com.shutter.photorize.domain.spot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.spot.dto.response.SpotResponse;
import com.shutter.photorize.domain.spot.repository.SpotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpotService {

	private final SpotRepository spotRepository;

	@Transactional(readOnly = true)
	public List<SpotResponse> getSpotsWithinBoundary(Double topLeftLat, Double topLeftLng, Double botRightLat,
		Double botRightLng, Member member) {
		return spotRepository.findSpotsWithinBoundary(topLeftLat, topLeftLng, botRightLat, botRightLng)
			.stream()
			.map(spot -> new SpotResponse(
				spot.getId(),
				spot.getSpotCode().getName(),
				spot.getName(),
				spot.getLatitude(),
				spot.getLongitude(),
				spotRepository.countFilesBySpotAndMember(spot, member),
				spotRepository.countMemoriesBySpotAndMember(spot, member)
			))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<Object> getFilesBySpot(Long spotId) {
		// Optional을 활용하여 존재하지 않을 경우 빈 리스트 반환
		return spotRepository.findById(spotId)
			.map(spot -> spotRepository.findFilesBySpot(spot))
			.orElse(List.of());
	}
}
