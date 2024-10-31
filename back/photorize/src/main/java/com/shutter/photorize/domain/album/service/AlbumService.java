package com.shutter.photorize.domain.album.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.dto.request.AlbumCreateRequest;
import com.shutter.photorize.domain.album.dto.response.AlbumDetailResponse;
import com.shutter.photorize.domain.album.dto.response.AlbumListResponse;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumMemberListRepository;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
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

	@Transactional
	public void createPublicAlbum(AlbumCreateRequest albumCreateRequest, MultipartFile albumImage, String email) {
		Member creator = memberRepository.getOrThrow(email);

		// Todo : 이미지 S3업로드
		String albumImageUrl = "";
		if (!albumImage.isEmpty()) {
		}

		Album savedAlbum = albumCreateRequest.toAlbum(creator, albumCreateRequest.getName(), albumImageUrl);
		albumRepository.save(savedAlbum);

		AlbumMemberList albumMemberList = albumCreateRequest.toList(savedAlbum, creator, true);
		albumMemberListRepository.save(albumMemberList);

		for (Long memberId : albumCreateRequest.getMembers()) {
			Member member = memberRepository.getOrThrow(memberId);
			albumMemberListRepository.save(albumCreateRequest.toList(savedAlbum, member, false));
		}
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

	public SliceResponse<AlbumDetailResponse> getAlbumDetail(Pageable pageable, Long memberId, Long albumId) {

		return;
	}
}
