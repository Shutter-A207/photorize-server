package com.shutter.photorize.domain.album.dto.response;

import java.util.List;

import com.shutter.photorize.domain.member.dto.MemberProfileDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumDetailResponse {
	private List<MemberProfileDto> members;

}
