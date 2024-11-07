package com.shutter.photorize.domain.spot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.repository.FileRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.spot.dto.response.SpotFileResponse;
import com.shutter.photorize.domain.spot.dto.response.SpotResponse;
import com.shutter.photorize.domain.spot.dto.response.SpotSearchResponse;
import com.shutter.photorize.domain.spot.dto.response.SpotWithFilesResponse;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.repository.SpotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpotService {

	private final SpotRepository spotRepository;
	private final MemberRepository memberRepository;
	private final FileRepository fileRepository;

	@Transactional(readOnly = true)
	public List<SpotResponse> getSpotsWithinBoundary(Double topLeftLat, Double topLeftLng,
		Double botRightLat, Double botRightLng, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

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
	public SpotWithFilesResponse getFilesBySpot(Long spotId, Long memberId) {
		Spot spot = spotRepository.getOrThrow(spotId);
		Member member = memberRepository.getOrThrow(memberId);

		List<File> files = spotRepository.findPhotoFilesByMemorySpotAndMember(spot, member);
		List<SpotFileResponse> fileResponses = files.stream()
			.map(SpotFileResponse::of)
			.collect(Collectors.toList());

		return SpotWithFilesResponse.of(
			spot.getId(),
			spot.getName(),
			spot.getLatitude(),
			spot.getLongitude(),
			fileResponses
		);
	}

	@Transactional(readOnly = true)
	public List<SpotResponse> getAllSpots(Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

		return spotRepository.findAll().stream()
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

	public List<SpotSearchResponse> searchSpotsByKeyword(String keyword) {
		List<Spot> spots = spotRepository.findByNameContaining(keyword);
		return spots.stream()
			.map(spot -> SpotSearchResponse.of(spot.getId(), spot.getName()))
			.collect(Collectors.toList());
	}
}
