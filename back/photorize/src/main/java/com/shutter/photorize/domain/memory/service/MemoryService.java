package com.shutter.photorize.domain.memory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.domain.comment.service.CommentService;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.request.MemoryCreateRequest;
import com.shutter.photorize.domain.memory.dto.request.MemoryUpdateRequest;
import com.shutter.photorize.domain.memory.dto.response.MemoryDetailResponse;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.repository.SpotRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoryService {

	private final MemoryRepository memoryRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;
	private final SpotRepository spotRepository;
	private final FileService fileService;
	private final CommentService commentService;

	@Transactional
	public void createMemory(Long memberId, MemoryCreateRequest memoryCreateRequest, List<MultipartFile> files) {
		Member member = memberRepository.getOrThrow(memberId);
		Spot spot = spotRepository.getOrThrow(memoryCreateRequest.getSpotId());
		Album album = getAlbum(member, memoryCreateRequest);
		Memory memory = memoryCreateRequest.toMemory(member, album, spot);

		memoryRepository.save(memory);
		fileService.saveFiles(files, memory);
		// TODO: 알림 구현해야합니다.
	}

	@Transactional(readOnly = true)
	public MemoryDetailResponse getMemoryDetail(Long memoryId) {
		Memory memory = memoryRepository.getMemoryWithMemberAndSpotById(memoryId);

		return MemoryDetailResponse.of(memory, fileService.getFilesByMemory(memory));
	}

	@Transactional(readOnly = true)
	public SliceResponse<CommentResponse> getCommentsByMemoryId(Long memoryId, Pageable pageable) {
		Memory memory = memoryRepository.getOrThrow(memoryId);

		return commentService.findCommentsWithMemberByMemory(
			memory, pageable);
	}

	@Transactional
	public void updateMemory(Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<MultipartFile> files) {
		Memory memory = memoryRepository.getOrThrow(memoryId);
		//TODO : 작성자만 수정 할 수 있도록 추가해야합니다.
		Spot spot = spotRepository.getOrThrow(memoryUpdateRequest.getSpotId());

		memory.updateContent(memoryUpdateRequest.getContent());
		memory.updateSpot(spot);
		memory.updateDate(memoryUpdateRequest.getDate().atStartOfDay());

		fileService.updateFile(files, memory);
	}

	@Transactional
	public void deleteMemory(Long memoryId) {
		Memory memory = memoryRepository.getOrThrow(memoryId);
		memory.softDelete();
		fileService.deleteFiles(memory);
	}

	public Album getAlbum(Member member, MemoryCreateRequest memoryCreateRequest) {
		return memoryCreateRequest.getType() == AlbumType.PRIVATE
			? albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND))
			: albumRepository.getOrThrow(memoryCreateRequest.getAlbumIds().get(0));
	}
}
