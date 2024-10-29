package com.shutter.photorize.domain.album.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.album.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
