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

import jakarta.persistence.LockModeType;

@Repository
public interface PoseRepository extends JpaRepository<Pose, Long> {

	@Query("""
		    SELECT new com.shutter.photorize.domain.pose.dto.response.PoseResponse(
		        p.id, 
		        p.headcount, 
		        p.img, 
		        COUNT(pl), 
		        CASE WHEN pl IS NOT NULL THEN true ELSE false END
		    )
		    FROM Pose p
		    LEFT JOIN PoseLike pl ON p.id = pl.pose.id AND pl.member.id = :memberId
		    GROUP BY p.id, p.headcount, p.img
		    ORDER BY COUNT(pl) DESC
		""")
	Page<PoseResponse> findAllWithLikes(@Param("memberId") Long memberId, Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Pose p WHERE p.id = :id")
	Pose getPoseWithLock(@Param("id") Long id);

	default Pose getOrThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new RuntimeException("Pose not found"));
	}
}
