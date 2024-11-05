package com.shutter.photorize.domain.comment.dto.request;

import com.shutter.photorize.domain.comment.entity.Comment;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.memory.entity.Memory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

	private Long memoryId;
	private String content;

	public Comment toComment(Member member, Memory memory) {
		return Comment.builder()
			.member(member)
			.memory(memory)
			.content(this.content)
			.build();
	}
}
