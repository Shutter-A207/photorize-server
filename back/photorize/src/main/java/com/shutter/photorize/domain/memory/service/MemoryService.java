package com.shutter.photorize.domain.memory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.request.MemoryCreateRequest;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.repository.SpotRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.util.S3Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoryService {

	private final MemoryRepository memoryRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;
	private final SpotRepository spotRepository;
	private final S3Utils s3Utils;

	@Transactional
	public void createMemory(Long memberId, MemoryCreateRequest memoryCreateRequest, List<MultipartFile> files) {

		Member member = memberRepository.getOrThrow(memberId);
		//FIXME: 추후에 Spot 에러 타입 정의 되면 수정해야합니다.
		Spot spot = spotRepository.findById(memoryCreateRequest.getSpot())
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_RESOURCE_FOUND));

		if (memoryCreateRequest.getType() == AlbumType.PRIVATE) {
			createPrivateMemory(member, memoryCreateRequest, spot);
		} else if (memoryCreateRequest.getType() == AlbumType.PUBLIC) {
			createPublicMemory(member, memoryCreateRequest, spot);
		}

		//TODO: 파일 생성 후 데이터베이스 저장 작업
	}

	private void createPrivateMemory(Member member, MemoryCreateRequest memoryCreateRequest, Spot spot) {

		Album album = albumRepository.findByMemberAndType(member, memoryCreateRequest.getType())
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND));
		Memory memory = memoryCreateRequest.toMemory(member, album, spot);
		memoryRepository.save(memory);

		//TODO: 알림 전송, 사용자별 앨범 추가 등의 작업을 여기에서 처리
	}

	private void createPublicMemory(Member member, MemoryCreateRequest memoryCreateRequest, Spot spot) {

		Album album = albumRepository.findById(memoryCreateRequest.getAlbums().get(0))
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND));
		Memory memory = memoryCreateRequest.toMemory(member, album, spot);
		memoryRepository.save(memory);

		//TODO: 알림 전송, 사용자별 앨범 추가 등의 작업을 여기에서 처리
	}

}
