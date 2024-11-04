package com.shutter.photorize.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shutter.photorize.domain.comment.entity.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {

	private Long commentId;
	private Long writerId;
	private String writerImg;
	private String nickname;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime date;

	@Builder
	private CommentResponse(Long commentId, Long writerId, String writerImg, String nickname, String content,
		LocalDateTime date) {
		this.commentId = commentId;
		this.writerId = writerId;
		this.writerImg = writerImg;
		this.nickname = nickname;
		this.content = content;
		this.date = date;
	}

	public static CommentResponse from(Comment comment) {
		return CommentResponse.builder()
			.commentId(comment.getId())
			.writerId(comment.getMember().getId())
			.writerImg(comment.getMember().getImg())
			.nickname(comment.getMember().getNickname())
			.content(comment.getContent())
			.date(comment.getCreatedAt())
			.build();
	}
}
