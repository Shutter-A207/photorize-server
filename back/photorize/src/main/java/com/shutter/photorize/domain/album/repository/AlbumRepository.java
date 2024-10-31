package com.shutter.photorize.domain.album.repository;

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

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

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
}
