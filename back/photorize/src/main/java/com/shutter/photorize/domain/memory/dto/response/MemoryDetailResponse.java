package com.shutter.photorize.domain.memory.dto.response;

import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemoryDetailResponse {
    
    private MemoryRepository memory;
    private List<CommentResponse> comments;
}
