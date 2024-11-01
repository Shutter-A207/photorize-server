package com.shutter.photorize.domain.file.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.entity.FileType;
import com.shutter.photorize.domain.file.repository.FileRepository;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.util.S3Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	private final S3Utils s3Utils;

	public void saveFile(List<MultipartFile> files, Memory memory) {
		files.forEach(file -> {
			String extension = getFileExtension(file);
			FileType type = FileType.fromExtension(extension);
			String url = s3Utils.uploadFile(file, type);
			fileRepository.save(File.of(memory, type, url));
		});
	}

	private String getFileExtension(MultipartFile file) {
		return StringUtils.getFilenameExtension(file.getOriginalFilename());
	}
}
