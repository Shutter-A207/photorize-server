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

@Service
public class PoseService {

	private final PoseRepository poseRepository;
	private final PoseLikeRepository poseLikeRepository;
	private final MemberRepository memberRepository;

	public PoseService(PoseRepository poseRepository, PoseLikeRepository poseLikeRepository,
		MemberRepository memberRepository) {
		this.poseRepository = poseRepository;
		this.poseLikeRepository = poseLikeRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional(readOnly = true)
	public List<PoseResponse> getAllPoses(Long memberId) {
		return poseRepository.findAll().stream().map(pose -> {
			boolean isLiked = poseLikeRepository.existsByMemberAndPose(memberRepository.findById(memberId)
				.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND)), pose);
			int likeCount = poseLikeRepository.countByPose(pose);
			return new PoseResponse(pose.getId(), pose.getHeadcount(), pose.getImg(), likeCount, isLiked);
		}).collect(Collectors.toList());
	}

	@Transactional
	public void likePose(Long poseId, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
		Pose pose = poseRepository.findById(poseId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_RESOURCE_FOUND));

		if (poseLikeRepository.existsByMemberAndPose(member, pose)) {
			throw new PhotorizeException(ErrorType.NO_RESOURCE_FOUND);
		}

		PoseLike poseLike = new PoseLike(member, pose);
		poseLikeRepository.save(poseLike);
	}

	@Transactional
	public void unlikePose(Long poseId, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
		Pose pose = poseRepository.findById(poseId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_RESOURCE_FOUND));

		PoseLike poseLike = poseLikeRepository.findByMemberAndPose(member, pose)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_RESOURCE_FOUND));

		poseLikeRepository.delete(poseLike);
	}
}
