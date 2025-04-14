package com.shutter.photorize.domain.memory.dto.request;

import java.time.LocalDate;

import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.spot.entity.Spot;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryUpdateRequest {

	@PastOrPresent
	private LocalDate date;

	private Long spotId;

	@Size(min = 1, max = 500)
	private String content;

	public void applyTo(Memory memory, Spot spot) {
		if (this.date != null) {
			memory.updateDate(this.date.atStartOfDay());
		}
		if (this.content != null) {
			memory.updateContent(this.content);
		}
		if (this.spotId != null) {
			memory.updateSpot(spot);
		}
	}
}
