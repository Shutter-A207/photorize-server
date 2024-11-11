package com.shutter.photorize.domain.comment.dto.response;

import com.shutter.photorize.domain.comment.entity.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateResponse {
	private Long commentId;

	@Builder
	private CommentCreateResponse(Long commentId) {
		this.commentId = commentId;
	}

	public static CommentCreateResponse from(Comment comment) {
		return CommentCreateResponse.builder()
			.commentId(comment.getId())
			.build();
	}
}
