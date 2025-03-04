package com.shutter.photorize.domain.album.service;

import static com.shutter.photorize.global.constant.CommonConstants.*;
import static com.shutter.photorize.global.constant.StringFormat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.alarm.service.InviteAlarmService;
import com.shutter.photorize.domain.album.dto.request.AlbumCreateRequest;
import com.shutter.photorize.domain.album.dto.request.AlbumModifyRequest;
import com.shutter.photorize.domain.album.dto.response.AlbumCreateResponse;
import com.shutter.photorize.domain.album.dto.response.AlbumDetailResponse;
import com.shutter.photorize.domain.album.dto.response.AlbumListResponse;
import com.shutter.photorize.domain.album.dto.response.AlbumSearchResponse;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.entity.Color;
import com.shutter.photorize.domain.album.repository.AlbumMemberListRepository;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.album.repository.ColorRepository;
import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.AlbumMemberProfileDto;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.dto.MemoryInfoDto;
import com.shutter.photorize.domain.memory.dto.request.MemoryCreateRequest;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
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
	private final MemoryRepository memoryRepository;
	private final ColorRepository colorRepository;
	private final InviteAlarmService inviteAlarmService;
	private final FileService fileService;

	@Transactional
	public AlbumCreateResponse createPublicAlbum(AlbumCreateRequest albumCreateRequest, Long memberId) {
		Member creator = memberRepository.getOrThrow(memberId);
		Color color = colorRepository.getOrThrow(albumCreateRequest.getColorId());

		Album savedAlbum = albumCreateRequest.toAlbum(creator, color, albumCreateRequest.getName(), AlbumType.PUBLIC);
		albumRepository.save(savedAlbum);

		AlbumMemberList albumMemberList = albumCreateRequest.toList(savedAlbum, creator, true);
		albumMemberListRepository.save(albumMemberList);

		List<AlbumMemberList> albumMembers = albumCreateRequest.getMembers().stream()
			.map(memberRepository::getOrThrow)
			.map(member -> albumCreateRequest.toList(savedAlbum, member, false))
			.collect(Collectors.toList());

		albumMemberListRepository.saveAll(albumMembers);

		inviteAlarmService.sendPublicAlarm(albumCreateRequest.getMembers(), savedAlbum, creator);

		return AlbumCreateResponse.of(savedAlbum);

	}

	@Transactional
	public void createPrivateAlbum(Long memberId) {
		Member creator = memberRepository.getOrThrow(memberId);
		Color color = colorRepository.getOrThrow(DEFAULT_ALBUM_COLOR);

		Album savedAlbum = Album.of(creator, color, PRIVATE_ALBUM_NAME_FORMAT, AlbumType.PRIVATE);
		albumRepository.save(savedAlbum);
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

	public List<AlbumListResponse> getAllAlbums(Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

		Album privateAlbum = albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
			.orElseThrow(() -> new PhotorizeException(
				ErrorType.NO_ALBUM_FOUND));

		List<Album> publicAlbums = albumRepository.findPublicAlbums(member);

		return Stream.concat(
				Stream.of(privateAlbum),
				publicAlbums.stream()
			)
			.map(AlbumListResponse::new)
			.toList();
	}

	public SliceResponse<AlbumDetailResponse> getAlbumDetail(Pageable pageable, Long albumId, Long memberId) {
		Album album = albumRepository.getOrThrow(albumId);
		Member member = memberRepository.getOrThrow(memberId);

		List<AlbumMemberList> albumMembers = albumMemberListRepository.findByAlbumAndMemberNotOrderByStatusDescMemberNicknameAsc(
			album, member);
		validateAlbumAccess(album, member);

		albumMembers.add(0, new AlbumMemberList(album, member, true));

		List<AlbumMemberProfileDto> albumMemberProfileDtoList = albumMembers.stream()
			.map(albumMember -> AlbumMemberProfileDto.from(albumMember.getMember(), albumMember.isStatus()))
			.toList();

		Slice<Object[]> memories = memoryRepository.findMemoryByWithFileByAlbum(album, pageable);

		List<MemoryInfoDto> memoryInfoDtos = memories.getContent().stream()
			.map(result -> {
				Memory memory = (Memory)result[0];
				File file = (File)result[1];

				String preSignedUrl = fileService.getPreSignedUrlByFile(file);

				return MemoryInfoDto.of(memory, preSignedUrl, memory.getSpot().getName());
			})
			.toList();

		AlbumDetailResponse albumDetail = AlbumDetailResponse.of(album.getId(), album.getName(),
			albumMemberProfileDtoList,
			memoryInfoDtos);

		return SliceResponse.of(new SliceImpl<>(
			List.of(albumDetail),
			pageable,
			memories.hasNext()
		));
	}

	@Transactional
	public void unfollowAlbum(Long albumId, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);
		Album album = albumRepository.getOrThrow(albumId);

		validateAlbumAccess(album, member);

		AlbumMemberList albumMemberList = albumMemberListRepository.getOrThrow(album, member);

		albumMemberList.updateStatus(false);

		if (checkMemberStatus(album)) {
			album.softDelete();
		}

	}

	public Album getAlbum(Member member, MemoryCreateRequest memoryCreateRequest) {
		return memoryCreateRequest.getType() == AlbumType.PRIVATE
			? albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND))
			: albumRepository.getOrThrow(memoryCreateRequest.getAlbumIds().get(0));
	}

	public void validateAlbumAccess(Album album, Member member) {
		boolean hasAccess = switch (album.getType()) {
			case PUBLIC -> albumMemberListRepository.existsByAlbumAndMember(album, member);
			case PRIVATE -> album.getMember().getId().equals(member.getId());
		};

		if (!hasAccess) {
			throw new PhotorizeException(ErrorType.NO_ALLOCATED_ALBUM);
		}
	}

	public List<AlbumSearchResponse> searchAlbum(String keyword, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

		List<Album> albums = albumRepository.findShareAlbums(keyword, member.getId());

		List<AlbumMemberList> albumMemberLists = albumMemberListRepository.findAlbumMembersByAlbumsAndMemberNot(albums,
			member);

		Map<Long, List<String>> albumMemberMap = albumMemberLists.stream()
			.collect(Collectors.groupingBy(
				aml -> aml.getAlbum().getId(),
				Collectors.mapping(
					aml -> aml.getMember().getNickname(),
					Collectors.toList()
				)
			));

		return albums.stream()
			.map(album -> AlbumSearchResponse.of(
				album,
				albumMemberMap.getOrDefault(album.getId(), new ArrayList<>())
			))
			.toList();
	}

	private boolean checkMemberStatus(Album album) {
		List<AlbumMemberList> albumMembers = albumMemberListRepository.findMembersByAlbum(album);

		boolean allMembersFalse = albumMembers.stream()
			.allMatch(member -> !member.isStatus());

		return allMembersFalse;
	}
}
