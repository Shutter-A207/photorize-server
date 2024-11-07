package com.shutter.photorize.domain.spot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

	default Spot getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.SPOT_NOT_FOUND));
	}

	@Query("SELECT s FROM Spot s WHERE s.latitude "
		+ "BETWEEN :botRightLat "
		+ "AND :topLeftLat "
		+ "AND s.longitude "
		+ "BETWEEN :topLeftLng "
		+ "AND :botRightLng")
	List<Spot> findSpotsWithinBoundary(
		@Param("topLeftLat") Double topLeftLat,
		@Param("topLeftLng") Double topLeftLng,
		@Param("botRightLat") Double botRightLat,
		@Param("botRightLng") Double botRightLng);

	@Query("SELECT COUNT(f) FROM File f WHERE f.memory.spot = :spot AND f.memory.member = :member")
	int countFilesBySpotAndMember(@Param("spot") Spot spot, @Param("member") Member member);

	@Query("SELECT COUNT(m) FROM Memory m WHERE m.spot = :spot AND m.member = :member")
	int countMemoriesBySpotAndMember(@Param("spot") Spot spot, @Param("member") Member member);

	@Query("SELECT f FROM File f JOIN f.memory m WHERE m.spot = :spot AND m.member = :member AND f.type = 'PHOTO'")
	List<File> findPhotoFilesByMemorySpotAndMember(@Param("spot") Spot spot, @Param("member") Member member);
}

