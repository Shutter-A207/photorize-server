package com.shutter.photorize.domain.album.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	default Album getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND));
	}

	Optional<Album> findByMemberAndType(Member member, AlbumType type);

	@Query("SELECT DISTINCT a " +
		"FROM Album a " +
		"JOIN AlbumMemberList aml ON aml.album = a " +
		"LEFT JOIN Memory d ON d.album = a " +
		"WHERE aml.member = :member " +
		"AND aml.status = true " +
		"AND a.type = :type " +
		"GROUP BY a " +
		"ORDER BY COALESCE(MIN(d.updatedAt), a.updatedAt) DESC")
	Slice<Album> findByAlbumMemberListMemberAndTypeOrderByEarliestMemory(@Param("member") Member member,
		@Param("type") AlbumType type,
		Pageable pageable);

	@Query("""
		SELECT a
		FROM Album a
		WHERE a.type = 'PUBLIC'
		   AND EXISTS (
		   SELECT 1
		   FROM AlbumMemberList aml
		   WHERE aml.album.id = a.id
		          AND aml.member.id = :memberId
		          AND aml.status = true
		   )
		AND(
		   a.name LIKE CONCAT('%', :keyword, '%')
		    OR EXISTS (
		       SELECT 1
		          FROM AlbumMemberList aml2
		            JOIN Member m On m.id = aml2.member.id
		          WHERE aml2.album.id = a.id
		                 AND m.id != :memberId
		                 AND m.nickname LIKE CONCAT('%', :keyword, '%')
		    )
		)
		""")
	List<Album> findShareAlbums(@Param("keyword") String keyword, @Param("memberId") Long memberId);

}
