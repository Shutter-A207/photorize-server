package com.shutter.photorize.domain.album.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.album.dto.request.AlbumCreateRequest;
import com.shutter.photorize.domain.album.dto.request.AlbumModifyRequest;
import com.shutter.photorize.domain.album.dto.response.AlbumDetailResponse;
import com.shutter.photorize.domain.album.dto.response.AlbumListResponse;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.entity.Color;
import com.shutter.photorize.domain.album.repository.AlbumMemberListRepository;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.album.repository.ColorRepository;
import com.shutter.photorize.domain.member.dto.MemberProfileDto;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.MemoryInfoDto;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.domain.memory.service.MemoryService;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumService {

	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;
	private final AlbumMemberListRepository albumMemberListRepository;
	private final MemoryService memoryService;
	private final MemoryRepository memoryRepository;
	private final ColorRepository colorRepository;

	@Transactional
	public void createPublicAlbum(AlbumCreateRequest albumCreateRequest, Long memberId) {
		Member creator = memberRepository.getOrThrow(memberId);
		Color color = colorRepository.getOrThrow(albumCreateRequest.getColorId());

		Album savedAlbum = albumCreateRequest.toAlbum(creator, color, albumCreateRequest.getName());
		albumRepository.save(savedAlbum);

		AlbumMemberList albumMemberList = albumCreateRequest.toList(savedAlbum, creator, true);
		albumMemberListRepository.save(albumMemberList);

		List<AlbumMemberList> albumMembers = albumCreateRequest.getMembers().stream()
			.map(memberRepository::getOrThrow)
			.map(member -> albumCreateRequest.toList(savedAlbum, member, false))
			.collect(Collectors.toList());

		albumMemberListRepository.saveAll(albumMembers);

	}

	@Transactional
	public void modifyAlbum(AlbumModifyRequest albumModifyRequest, Long albumId, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);
		Album album = albumRepository.getOrThrow(albumId);
		Color color = colorRepository.getOrThrow(albumModifyRequest.getColorId());

		validateAlbumAccess(album, member);
		album.updateName(albumModifyRequest.getName());
		album.updateColor(color);
	}

	public Slice<AlbumListResponse> getAllalbums(Pageable pageable, Long memberId) {

		Member member = memberRepository.getOrThrow(memberId);

		Slice<Album> publicAlbums = albumRepository.findByAlbumMemberListMemberAndTypeOrderByEarliestMemory(member,
			AlbumType.PUBLIC,
			pageable);

		List<AlbumListResponse> publicAlbumInfos = publicAlbums.getContent().stream()
			.map(AlbumListResponse::new)
			.toList();

		if (pageable.getPageNumber() == 0) {
			Album privateAlbum = albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
				.orElseThrow(() -> new PhotorizeException(
					ErrorType.NO_ALBUM_FOUND));

			List<AlbumListResponse> result = new ArrayList<>();
			result.add(new AlbumListResponse(privateAlbum));
			result.addAll(publicAlbumInfos);
			return new SliceImpl<>(result, pageable, publicAlbums.hasNext());
		}

		return new SliceImpl<>(publicAlbumInfos, pageable, publicAlbums.hasNext());
	}

	public SliceResponse<AlbumDetailResponse> getAlbumDetail(Pageable pageable, Long albumId, Long memberId) {
		Album album = albumRepository.getOrThrow(albumId);

		List<AlbumMemberList> albumMembers = albumMemberListRepository.findMembersByAlbum(album);

		isAllcatedAlbumMember(albumMembers, memberId);

		List<MemberProfileDto> memberProfileDtoList = albumMembers.stream()
			.map(albumMember -> MemberProfileDto.from(albumMember.getMember(), albumMember.isStatus()))
			.toList();

		Slice<MemoryInfoDto> memories = memoryRepository.findMemoryInfoDtosByAlbum(album, pageable);

		AlbumDetailResponse albumDetail = AlbumDetailResponse.of(album.getName(), memberProfileDtoList,
			memories.getContent());

		return SliceResponse.of(new SliceImpl<>(
			List.of(albumDetail),
			pageable,
			memories.hasNext()
		));
	}

	private void isAllcatedAlbumMember(List<AlbumMemberList> albumMemberLists, Long memberId) {
		boolean isMember = albumMemberLists.stream()
			.anyMatch(albumMemberList -> albumMemberList.getMember().getId().equals(memberId));

		if (!isMember) {
			throw new PhotorizeException(ErrorType.NO_ALLOCATED_ALBUM);
		}
	}

	private void validateAlbumAccess(Album album, Member member) {
		boolean hasAccess = switch (album.getType()) {
			case PUBLIC -> albumMemberListRepository.existsByAlbumAndMember(album, member);
			case PRIVATE -> album.getMember().getId().equals(member.getId());
		};

		if (!hasAccess) {
			throw new PhotorizeException(ErrorType.NO_ALLOCATED_ALBUM);
		}
	}
}
