package com.shutter.photorize.domain.memory.service;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.request.MemoryCreateRequest;
import com.shutter.photorize.domain.memory.dto.response.MemoryDetailResponse;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.domain.spot.repository.SpotRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final SpotRepository spotRepository;
    private final FileService fileService;

    @Transactional
    public void createMemory(Long memberId, MemoryCreateRequest memoryCreateRequest, List<MultipartFile> files) {

        Member member = memberRepository.getOrThrow(memberId);
        // FIXME: 추후에 Spot 에러 타입 정의 되면 수정해야합니다.
        Spot spot = spotRepository.findById(memoryCreateRequest.getSpotId())
                .orElseThrow(() -> new PhotorizeException(ErrorType.NO_RESOURCE_FOUND));
        Album album = getAlbum(member, memoryCreateRequest);
        Memory memory = memoryCreateRequest.toMemory(member, album, spot);

        memoryRepository.save(memory);
        fileService.saveFile(files, memory);
        // TODO: 알림 구현해야합니다.
    }

    @Transactional(readOnly = true)
    public MemoryDetailResponse getMemoryDetail(Long memoryId) {
        // TODO: 추억 조회 구현 필요

        return null;
    }

    public Album getAlbum(Member member, MemoryCreateRequest memoryCreateRequest) {
        return memoryCreateRequest.getType() == AlbumType.PRIVATE
                ? albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
                .orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND))
                : albumRepository.getOrThrow(memoryCreateRequest.getAlbumIds().get(0));
    }
}
