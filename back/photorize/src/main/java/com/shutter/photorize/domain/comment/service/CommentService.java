package com.shutter.photorize.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.comment.dto.request.CommentCreateRequest;
import com.shutter.photorize.domain.comment.dto.request.CommentUpdateRequest;
import com.shutter.photorize.domain.comment.dto.response.CommentCreateResponse;
import com.shutter.photorize.domain.comment.dto.response.CommentResponse;
import com.shutter.photorize.domain.comment.entity.Comment;
import com.shutter.photorize.domain.comment.repository.CommentRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final MemoryRepository memoryRepository;

	public SliceResponse<CommentResponse> findCommentsWithMemberByMemory(Memory memory, Pageable pageable) {
		Slice<CommentResponse> comments = commentRepository.findCommentsWithMemberByMemory(memory, pageable)
			.map(CommentResponse::from);
		return SliceResponse.of(comments);
	}

	@Transactional
	public CommentCreateResponse createComment(Long memberId, CommentCreateRequest commentCreateRequest) {
		Member member = memberRepository.getOrThrow(memberId);
		Memory memory = memoryRepository.getOrThrow(commentCreateRequest.getMemoryId());

		Comment comment = commentRepository.save(commentCreateRequest.toComment(member, memory));
		return CommentCreateResponse.from(comment);
	}

	@Transactional
	public void updateComment(Long memberId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
		Comment comment = commentRepository.getOrThrow(commentId);

		validateAuthor(memberId, comment);

		comment.updateContent(commentUpdateRequest.getContent());
	}

	@Transactional
	public void deleteComment(Long memberId, Long commentId) {
		Comment comment = commentRepository.getOrThrow(commentId);

		validateAuthor(memberId, comment);

		commentRepository.delete(comment);
	}

	private void validateAuthor(Long memberId, Comment comment) {
		if (!comment.getMember().getId().equals(memberId)) {
			throw new PhotorizeException(ErrorType.COMMENT_FORBIDDEN);
		}
	}

}
