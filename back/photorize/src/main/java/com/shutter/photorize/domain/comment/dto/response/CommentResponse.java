package com.shutter.photorize.domain.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long commentId;
    private Long writerId;
    private String writerImg;
    private String nickname;
    private String content;
    private LocalDate date;
}
