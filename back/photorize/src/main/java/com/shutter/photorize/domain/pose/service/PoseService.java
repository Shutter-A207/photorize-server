package com.shutter.photorize.domain.pose.service;

import java.util.List;
import java.util.stream.Collectors;

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

	private Member getMemberOrThrow(Long memberId) {
		if (memberId == null) {
			throw new PhotorizeException(ErrorType.USER_NOT_FOUND);
		}
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
	}

	private Pose getPoseOrThrow(Long poseId) {
		return poseRepository.findById(poseId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_POSE_FOUND));
	}

	@Transactional(readOnly = true)
	public List<PoseResponse> getAllPoses(Long memberId) {
		Member member = getMemberOrThrow(memberId);
		return poseRepository.findAll().stream().map(pose -> {
			boolean isLiked = poseLikeRepository.existsByMemberAndPose(member, pose);
			int likeCount = poseLikeRepository.countByPose(pose);
			return new PoseResponse(pose.getId(), pose.getHeadcount().name(), pose.getImg(),
				likeCount, isLiked);
		}).collect(Collectors.toList());
	}

	@Transactional
	public void likePose(Long poseId, Long memberId) {
		Member member = getMemberOrThrow(memberId);
		Pose pose = getPoseOrThrow(poseId);

		if (poseLikeRepository.existsByMemberAndPose(member, pose)) {
			throw new PhotorizeException(ErrorType.NO_POSE_FOUND);
		}

		PoseLike poseLike = new PoseLike(member, pose);
		poseLikeRepository.save(poseLike);
	}

	@Transactional
	public void unlikePose(Long poseId, Long memberId) {
		Member member = getMemberOrThrow(memberId);
		Pose pose = getPoseOrThrow(poseId);

		PoseLike poseLike = poseLikeRepository.findByMemberAndPose(member, pose)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_POSE_FOUND));

		poseLikeRepository.delete(poseLike);
	}
}
