package com.shutter.photorize.domain.spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.spot.entity.Spot;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
}
