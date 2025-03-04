package com.shutter.photorize.domain.memory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.member.entity.Member;
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

	Slice<Memory> findMemoryByAlbum(Album album, Pageable pageable);

	@Query("SELECT m FROM Memory m " +
		"JOIN FETCH m.member " +
		"LEFT JOIN FETCH m.spot " +
		"WHERE m.id = :memoryId")
	Optional<Memory> findMemoryWithMemberAndSpotById(@Param("memoryId") Long memoryId);

	@Query("""
		SELECT DISTINCT m, f.url
		FROM Memory m
		JOIN FETCH m.album a
		LEFT JOIN File f ON f.memory = m AND f.type = 'PHOTO' 
		WHERE (a.member = :member AND a.type = 'PRIVATE') 
		OR EXISTS (
		    SELECT aml FROM AlbumMemberList aml 
		    WHERE aml.album.id = a.id 
		    AND aml.member = :member 
		    AND aml.status = true
		) 
		ORDER BY FUNCTION('RAND') 
		LIMIT 8
		""")
	List<Object[]> findMemoryRandom(@Param("member") Member member);
}
