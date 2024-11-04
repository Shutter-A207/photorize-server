package com.shutter.photorize.domain.memory.dto.response;

import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemoryDetailResponse {

	private MemoryResponse memory;
	private SliceResponse<CommentResponse> comments;

	@Builder
	private MemoryDetailResponse(MemoryResponse memory, SliceResponse<CommentResponse> comments) {
		this.memory = memory;
		this.comments = comments;
	}

	public static MemoryDetailResponse of(MemoryResponse memory, SliceResponse<CommentResponse> comments) {
		return MemoryDetailResponse.builder()
			.memory(memory)
			.comments(comments)
			.build();
	}
}
