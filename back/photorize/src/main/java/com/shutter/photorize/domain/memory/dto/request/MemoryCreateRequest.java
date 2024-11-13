package com.shutter.photorize.domain.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.spot.entity.Spot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryCreateRequest {

	@NotNull
	@PastOrPresent
	private LocalDate date;

	@NotNull
	private Long spotId;

	@NotBlank
	@Size(min = 1, max = 500)
	private String content;

	@NotEmpty
	private List<Long> albumIds;

	@NotNull
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
