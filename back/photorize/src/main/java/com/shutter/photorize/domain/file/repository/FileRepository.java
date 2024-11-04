package com.shutter.photorize.domain.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.memory.entity.Memory;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findFilesByMemory(Memory memory);
}
