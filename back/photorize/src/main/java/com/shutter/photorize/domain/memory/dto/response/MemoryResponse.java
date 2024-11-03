package com.shutter.photorize.domain.memory.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MemoryResponse {

    private Long writerId;
    private String nickname;
    private String writerImg;
    private List<String> files;
    private LocalDate date;
    private String spotName;
    private String content;
}
