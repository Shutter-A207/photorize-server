package com.shutter.photorize.domain.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.spot.entity.Spot;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryCreateRequest {
	private LocalDate date;
	private Long spot; //spot 엔티티
	private String content;
	private List<Long> albums; //앨범 엔티티
	private AlbumType type;

	public Memory toMemory(Member member, Album album, Spot spot) {
		return Memory.builder()
			.member(member)
			.album(album)
			.spot(spot)
			.date(this.date.atStartOfDay())
			.content(this.content)
			.build();
	}
}
