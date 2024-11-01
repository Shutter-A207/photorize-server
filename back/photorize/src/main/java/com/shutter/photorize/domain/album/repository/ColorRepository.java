package com.shutter.photorize.domain.album.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.album.entity.Color;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

	default Color getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.NO_COLOR_FOUND));
	}
}
