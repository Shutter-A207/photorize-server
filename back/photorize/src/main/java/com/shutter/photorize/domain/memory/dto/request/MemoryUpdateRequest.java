package com.shutter.photorize.domain.memory.dto.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryUpdateRequest {
	private LocalDate date;
	private Long spotId;
	private String content;
}
