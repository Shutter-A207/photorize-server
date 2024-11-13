package com.shutter.photorize.domain.album.dto.request;

import java.util.List;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.entity.Color;
import com.shutter.photorize.domain.member.entity.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumCreateRequest {

	@NotBlank
	@Size(min = 2, max = 12)
	private String name;

	@NotNull
	private Long colorId;

	@NotNull
	@Size(min = 1)
	private List<Long> members;

	public Album toAlbum(Member member, Color color, String name, AlbumType type) {
		return Album.builder()
			.member(member)
			.color(color)
			.name(name)
			.type(type)
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