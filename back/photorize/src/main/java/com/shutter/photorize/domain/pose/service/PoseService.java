package com.shutter.photorize.domain.pose.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.pose.dto.response.PoseResponse;
import com.shutter.photorize.domain.pose.entity.Pose;
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
	public Slice<PoseResponse> getAllPoses(Long memberId, int page, int size) {
		Slice<PoseResponse> poses = poseRepository.findAllWithLikes(memberId, PageRequest.of(page, size));
		return poses;
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
