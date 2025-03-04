package com.shutter.photorize.domain.memory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.alarm.service.InviteAlarmService;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.domain.comment.service.CommentService;
import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.repository.FileRepository;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.request.MemoryCreateRequest;
import com.shutter.photorize.domain.memory.dto.request.MemoryUpdateRequest;
import com.shutter.photorize.domain.memory.dto.response.MainMemoryResponse;
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
	private final SpotRepository spotRepository;
	private final FileService fileService;
	private final CommentService commentService;
	private final AlbumService albumService;
	private final InviteAlarmService inviteAlarmService;
	private final FileRepository fileRepository;

	@Transactional
	public void createMemory(Long memberId, MemoryCreateRequest memoryCreateRequest, List<MultipartFile> files) {
		Member member = memberRepository.getOrThrow(memberId);
		Spot spot = spotRepository.getOrThrow(memoryCreateRequest.getSpotId());
		Album album = albumService.getAlbum(member, memoryCreateRequest);
		Memory memory = memoryCreateRequest.toMemory(member, album, spot);

		memoryRepository.save(memory);
		fileService.saveFiles(files, memory);
		if (album.getType() == AlbumType.PRIVATE) {
			inviteAlarmService.sendPrivateAlarm(memoryCreateRequest.getAlbumIds(), memory, member);
		}
	}

	@Transactional(readOnly = true)
	public MemoryDetailResponse getMemoryDetail(Long memberId, Long memoryId) {
		Member member = memberRepository.getOrThrow(memberId);
		Memory memory = memoryRepository.getMemoryWithMemberAndSpotById(memoryId);
		albumService.validateAlbumAccess(memory.getAlbum(), member);

		return MemoryDetailResponse.of(memory, fileService.getFilesByMemory(memory));
	}

	@Transactional(readOnly = true)
	public SliceResponse<CommentResponse> getCommentsByMemoryId(Long memberId, Long memoryId, Pageable pageable) {
		Member member = memberRepository.getOrThrow(memberId);
		Memory memory = memoryRepository.getOrThrow(memoryId);
		albumService.validateAlbumAccess(memory.getAlbum(), member);

		return commentService.findCommentsWithMemberByMemory(
			memory, pageable);
	}

	@Transactional
	public void updateMemory(Long memberId, Long memoryId, MemoryUpdateRequest memoryUpdateRequest,
		List<MultipartFile> files) {
		Memory memory = memoryRepository.getOrThrow(memoryId);
		Member member = memberRepository.getOrThrow(memberId);

		validateWriter(memory, member);

		Spot spot = spotRepository.getOrThrow(memoryUpdateRequest.getSpotId());

		memory.updateContent(memoryUpdateRequest.getContent());
		memory.updateSpot(spot);
		memory.updateDate(memoryUpdateRequest.getDate().atStartOfDay());

		fileService.updateFile(files, memory);
	}

	@Transactional
	public void deleteMemory(Long memberId, Long memoryId) {
		Memory memory = memoryRepository.getOrThrow(memoryId);
		Member member = memberRepository.getOrThrow(memberId);

		validateWriter(memory, member);

		memory.softDelete();
		fileService.deleteFiles(memory);
	}

	private void validateWriter(Memory memory, Member member) {
		if (!memory.getMember().equals(member)) {
			throw new PhotorizeException(ErrorType.MEMORY_FORBIDDEN);
		}
	}

	public List<MainMemoryResponse> getMainMemory(Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

		List<Object[]> memories = memoryRepository.findMemoryRandom(member);

		return memories.stream()
			.map(result -> {
				Memory memory = (Memory)result[0];
				File file = (File)result[1];

				return MainMemoryResponse.of(memory, fileService.getPreSignedUrlByFile(file));
			})
			.toList();

	}
}
