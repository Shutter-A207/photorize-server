package com.shutter.photorize.domain.memory.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryUpdateRequest {

	@NotNull
	@PastOrPresent
	private LocalDate date;

	@NotNull
	private Long spotId;

	@NotBlank
	@Size(min = 1, max = 500)
	private String content;
}
