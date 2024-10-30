package com.shutter.photorize.domain.album.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shutter.photorize.domain.album.entity.AlbumMemberList;

public interface AlbumMemberListRepository extends JpaRepository<AlbumMemberList, Long> {
}
