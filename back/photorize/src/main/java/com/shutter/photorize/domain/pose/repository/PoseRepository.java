package com.shutter.photorize.domain.pose.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.pose.dto.response.PoseResponse;
import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import jakarta.persistence.LockModeType;

@Repository
public interface PoseRepository extends JpaRepository<Pose, Long> {

	@Query(value = """
		SELECT p.id AS poseId, 
		       p.headcount AS headcount, 
		       p.img AS img, 
		       (SELECT COUNT(*) FROM pose_like pl WHERE pl.pose_id = p.id) AS likeCount,
		       EXISTS (
		           SELECT 1 FROM pose_like pl WHERE pl.pose_id = p.id AND pl.member_id = :memberId
		       ) AS isLiked
		FROM pose p
		ORDER BY likeCount DESC
		""", nativeQuery = true)
	Page<PoseResponse> findAllWithLikes(@Param("memberId") Long memberId, Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Pose p WHERE p.id = :id")
	Pose getPoseWithLock(@Param("id") Long id);

	default Pose getOrThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_POSE_FOUND));
	}
}
