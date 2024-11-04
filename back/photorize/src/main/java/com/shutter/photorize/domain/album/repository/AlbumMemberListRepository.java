package com.shutter.photorize.domain.album.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.member.entity.Member;

public interface AlbumMemberListRepository extends JpaRepository<AlbumMemberList, Long> {
	@Query("SELECT aml FROM AlbumMemberList aml " +
		"JOIN aml.member m " +
		"WHERE aml.album = :album " +
		"ORDER BY aml.status DESC, m.nickname ASC")
	List<AlbumMemberList> findMembersByAlbum(Album album);

	boolean existsByAlbumAndMember(Album album, Member member);
}
