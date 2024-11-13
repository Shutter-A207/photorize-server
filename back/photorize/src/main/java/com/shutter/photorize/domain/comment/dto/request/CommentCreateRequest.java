package com.shutter.photorize.domain.comment.dto.request;

import com.shutter.photorize.domain.comment.entity.Comment;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.memory.entity.Memory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

	@NotBlank
	private Long memoryId;

	@NotBlank
	@Size(min = 1, max = 100)
	private String content;

	public Comment toComment(Member member, Memory memory) {
		return Comment.builder()
			.member(member)
			.memory(memory)
			.content(this.content)
			.build();
	}
}
