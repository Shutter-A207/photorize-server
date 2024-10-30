package com.shutter.photorize.domain.album.dto.request;

import java.util.List;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateRequest {
	private String name;
	private List<Long> members;

	@Builder
	public AlbumCreateRequest(String name, List<Long> members) {
		this.name = name;
		this.members = members;
	}

	public Album toAlbum(Member member, String name, String imgUrl) {
		return Album.builder()
			.member(member)
			.name(name)
			.type(AlbumType.PUBLIC)
			.img(imgUrl)
			.build();
	}

	public AlbumMemberList toList(Album album, Member member, boolean status) {
		return AlbumMemberList.builder()
			.album(album)
			.member(member)
			.status(status)
			.build();
	}
}