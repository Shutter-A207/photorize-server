package com.shutter.photorize.domain.file.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.file.dto.response.FileResponse;
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

	public void saveFile(MultipartFile file, Memory memory) {
		String extension = getFileExtension(file);
		FileType type = FileType.fromExtension(extension);
		String url = s3Utils.uploadFile(file, type);

		fileRepository.save(File.of(memory, type, url));
	}

	public void saveFiles(List<MultipartFile> files, Memory memory) {
		files.forEach(file -> saveFile(file, memory));
	}

	public List<FileResponse> getFilesByMemory(Memory memory) {
		return convertFilesToResponses(fileRepository.findFilesByMemory(memory));
	}

	public void updateFile(List<MultipartFile> files, Memory memory) {
		if (hasNewFiles(files)) {
			files.forEach(file -> {
				String extension = getFileExtension(file);
				FileType type = FileType.fromExtension(extension);
				deleteFileByType(memory, type);
				saveFile(file, memory);
			});
		}
	}

	public void deleteFiles(Memory memory) {
		List<File> existingFiles = fileRepository.findFilesByMemory(memory);
		fileRepository.deleteAll(existingFiles);
	}

	public void deleteFileByType(Memory memory, FileType type) {
		File file = fileRepository.getFilesByMemoryAndTypeOrThrow(memory, type);
		fileRepository.delete(file);
	}

	public String getDefaultProfile() {
		return "https://photorize-upload.s3.ap-northeast-2.amazonaws.com/member/default.jpg";
	}

	private String getFileExtension(MultipartFile file) {
		return StringUtils.getFilenameExtension(file.getOriginalFilename());
	}

	private List<FileResponse> convertFilesToResponses(List<File> files) {
		return files.stream()
			.map(FileResponse::from)
			.toList();
	}

	private boolean hasNewFiles(List<MultipartFile> files) {
		return !files.isEmpty();
	}

}
