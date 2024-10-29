package com.shutter.photorize.domain.spot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.spot.entity.Spot;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

	@Query("SELECT s FROM Spot s WHERE s.latitude "
		+ "BETWEEN :botRightLat AND :topLeftLat AND s.longitude BETWEEN :topLeftLng AND :botRightLng")
	List<Spot> findSpotsWithinBoundary(@Param("topLeftLat") Double topLeftLat,
		@Param("topLeftLng") Double topLeftLng,
		@Param("botRightLat") Double botRightLat,
		@Param("botRightLng") Double botRightLng);

	@Query("SELECT f FROM File f JOIN f.diary d WHERE d.spot.id = :spotId")
	List<Object> findFilesBySpotId(@Param("spotId") Long spotId);
}
