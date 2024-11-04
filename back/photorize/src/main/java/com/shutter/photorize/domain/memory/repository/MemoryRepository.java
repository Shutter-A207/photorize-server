package com.shutter.photorize.domain.memory.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.memory.dto.MemoryInfoDto;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

	default Memory getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.MEMORY_NOT_FOUND));
	}

	default Memory getMemoryWithMemberAndSpotById(Long id) {
		return findMemoryWithMemberAndSpotById(id).orElseThrow(
			() -> new PhotorizeException(ErrorType.MEMORY_NOT_FOUND));
	}

	@Query("SELECT new com.shutter.photorize.domain.memory.dto.MemoryInfoDto(" +
		"m.id, f.url, s.name, m.date) " +
		"FROM Memory m " +
		"LEFT JOIN m.spot s " +
		"LEFT JOIN File f ON f.memory = m " +
		"WHERE m.album = :album " +
		"ORDER BY m.date DESC")
	Slice<MemoryInfoDto> findMemoryInfoDtosByAlbum(Album album, Pageable pageable);

	@Query("SELECT m FROM Memory m " +
		"JOIN FETCH m.member " +
		"LEFT JOIN FETCH m.spot " +
		"WHERE m.id = :memoryId")
	Optional<Memory> findMemoryWithMemberAndSpotById(@Param("memoryId") Long memoryId);
}
