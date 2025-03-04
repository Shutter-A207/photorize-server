package com.shutter.photorize.domain.file.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.entity.FileType;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

	default File getFilesByMemoryAndTypeOrThrow(Memory memory, FileType type) {
		return findFilesByMemoryAndType(memory, type).orElseThrow(
			() -> new PhotorizeException(ErrorType.FILE_NOT_FOUND));
	}

	List<File> findFilesByMemory(Memory memory);

	Optional<File> findFilesByMemoryAndType(Memory memory, FileType type);

	Optional<File> findFirstByMemoryAndTypeOrderByIdAsc(Memory memory, FileType type);
}
