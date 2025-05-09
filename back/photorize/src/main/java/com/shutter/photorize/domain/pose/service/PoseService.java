package com.shutter.photorize.domain.pose.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.pose.dto.response.PoseResponse;
import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.domain.pose.entity.PoseHeadcount;
import com.shutter.photorize.domain.pose.entity.PoseLike;
import com.shutter.photorize.domain.pose.repository.PoseLikeRepository;
import com.shutter.photorize.domain.pose.repository.PoseRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PoseService {

	private final PoseRepository poseRepository;
	private final PoseLikeRepository poseLikeRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Slice<PoseResponse> getAllPoses(Long memberId, Pageable pageable) {
		return poseRepository.findAllWithLikes(memberId, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<PoseResponse> getPosesByHeadcount(Long memberId, String headcount, Pageable pageable) {
		PoseHeadcount headcountEnum;
		try {
			headcountEnum = PoseHeadcount.valueOf(headcount.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new PhotorizeException(ErrorType.NO_POSE_FOUND);
		}
		return poseRepository.findByHeadcountWithLikes(memberId, headcountEnum, pageable);
	}

	@Transactional
	public void likePose(Long poseId, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);
		Pose pose = poseRepository.getPoseWithLock(poseId);

		if (poseLikeRepository.existsByMemberAndPose(member, pose)) {
			throw new PhotorizeException(ErrorType.ALREADY_LIKED);
		}

		poseLikeRepository.save(new PoseLike(member, pose));
	}

	@Transactional
	public void unlikePose(Long poseId, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);
		Pose pose = poseRepository.getPoseWithLock(poseId);

		PoseLike poseLike = poseLikeRepository.findByMemberAndPose(member, pose)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_POSE_FOUND));

		poseLikeRepository.delete(poseLike);
	}
}
