package com.shutter.photorize.domain.pose.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.domain.pose.entity.PoseLike;
import com.shutter.photorize.domain.pose.repository.PoseLikeRepository;
import com.shutter.photorize.domain.pose.repository.PoseRepository;

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
	public List<Pose> getAllPoses() {
		return poseRepository.findAll();
	}

	@Transactional
	public void likePose(Long poseId, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		Pose pose = poseRepository.findById(poseId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포즈입니다."));

		if (poseLikeRepository.existsByMemberAndPose(member, pose)) {
			throw new IllegalStateException("이미 좋아요한 포즈입니다.");
		}

		PoseLike poseLike = new PoseLike(member, pose);
		poseLikeRepository.save(poseLike);
	}

	@Transactional
	public void unlikePose(Long poseId, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
		Pose pose = poseRepository.findById(poseId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포즈입니다."));

		PoseLike poseLike = poseLikeRepository.findByMemberAndPose(member, pose)
			.orElseThrow(() -> new IllegalStateException("좋아요하지 않은 포즈입니다."));

		poseLikeRepository.delete(poseLike);
	}
}
