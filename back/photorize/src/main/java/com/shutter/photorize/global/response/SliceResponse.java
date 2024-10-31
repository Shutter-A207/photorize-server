package com.shutter.photorize.global.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceResponse<T> {
	private List<T> content;
	private int currentPage;
	private boolean first;
	private boolean hasNext;

	@Builder
	private SliceResponse(List<T> content, int currentPage, boolean first, boolean hasNext) {
		this.content = content;
		this.currentPage = currentPage;
		this.first = first;
		this.hasNext = hasNext;
	}

	public static <T> SliceResponse<T> of(Slice<T> slice) {
		return SliceResponse.<T>builder()
			.content(slice.getContent())
			.currentPage(slice.getNumber())
			.first(slice.isFirst())
			.hasNext(slice.hasNext())
			.build();
	}
}
