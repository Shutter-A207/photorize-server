package com.shutter.photorize.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.file.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
