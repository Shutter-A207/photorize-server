package com.shutter.photorize.domain.pose.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.domain.pose.entity.PoseLike;

public interface PoseLikeRepository extends JpaRepository<PoseLike, Long> {
	boolean existsByMemberAndPose(Member member, Pose pose);

	Optional<PoseLike> findByMemberAndPose(Member member, Pose pose);
}
