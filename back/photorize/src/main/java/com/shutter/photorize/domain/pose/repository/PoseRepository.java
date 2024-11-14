package com.shutter.photorize.domain.pose.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.pose.dto.response.PoseResponse;
import com.shutter.photorize.domain.pose.entity.Pose;
import com.shutter.photorize.domain.pose.entity.PoseHeadcount;

import jakarta.persistence.LockModeType;

@Repository
public interface PoseRepository extends JpaRepository<Pose, Long> {

	@Query("""
		    SELECT new com.shutter.photorize.domain.pose.dto.response.PoseResponse(
		        p.id,
		        p.headcount,
		        p.img,
		        (SELECT COUNT(pl) FROM PoseLike pl WHERE pl.pose.id = p.id),
		        CASE WHEN EXISTS (SELECT 1 FROM PoseLike pl WHERE pl.pose.id = p.id AND pl.member.id = :memberId) THEN true ELSE false END
		    )
		    FROM Pose p
		    GROUP BY p.id, p.headcount, p.img
		    ORDER BY (SELECT COUNT(pl) FROM PoseLike pl WHERE pl.pose.id = p.id) DESC
		""")
	Slice<PoseResponse> findAllWithLikes(@Param("memberId") Long memberId, Pageable pageable);

	@Query("""
		    SELECT new com.shutter.photorize.domain.pose.dto.response.PoseResponse(
		        p.id,
		        p.headcount,
		        p.img,
		        (SELECT COUNT(pl) FROM PoseLike pl WHERE pl.pose.id = p.id),
		        CASE WHEN EXISTS (SELECT 1 FROM PoseLike pl WHERE pl.pose.id = p.id AND pl.member.id = :memberId) 
		        THEN true ELSE false END
		    )
		    FROM Pose p
		    WHERE (:headcount IS NULL OR p.headcount = :headcount)
		    GROUP BY p.id, p.headcount, p.img
		    ORDER BY (SELECT COUNT(pl) FROM PoseLike pl WHERE pl.pose.id = p.id) DESC
		""")
	Slice<PoseResponse> findByHeadcountWithLikes(
		@Param("memberId") Long memberId,
		@Param("headcount") PoseHeadcount headcount, // ENUM 타입으로 변경
		Pageable pageable
	);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Pose p WHERE p.id = :id")
	Pose getPoseWithLock(@Param("id") Long id);

	default Pose getOrThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new RuntimeException("Pose not found"));
	}
}
