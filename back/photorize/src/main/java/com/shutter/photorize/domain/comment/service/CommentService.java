package com.shutter.photorize.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.domain.comment.repository.CommentRepository;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;

	public SliceResponse<CommentResponse> findCommentsWithMemberByMemory(Memory memory, Pageable pageable) {
		Slice<CommentResponse> comments = commentRepository.findCommentsWithMemberByMemory(memory, pageable)
			.map(CommentResponse::from);
		return SliceResponse.of(comments);
	}
}
