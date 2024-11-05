package com.shutter.photorize.domain.spot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
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
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public List<SpotResponse> getSpotsWithinBoundary(Double topLeftLat, Double topLeftLng, Double botRightLat,
		Double botRightLng, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));

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
	public List<Object> getFilesBySpot(Long spotId, Long memberId) {
		Spot spot = spotRepository.findById(spotId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.SPOT_NOT_FOUND));  // Spot이 없으면 예외 발생
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));  // Member가 없으면 예외 발생
		return spotRepository.findFilesBySpot(spot, member); // 특정 사용자의 파일만 조회
	}
}
