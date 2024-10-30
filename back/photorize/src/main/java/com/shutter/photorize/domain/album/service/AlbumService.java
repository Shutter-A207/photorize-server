package com.shutter.photorize.domain.album.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.dto.request.AlbumCreateRequest;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.repository.AlbumMemberListRepository;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
}
