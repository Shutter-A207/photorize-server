package com.shutter.photorize.domain.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.comment.dto.request.CommentCreateRequest;
import com.shutter.photorize.domain.comment.dto.request.CommentUpdateRequest;
import com.shutter.photorize.domain.comment.dto.response.CommentCreateResponse;
import com.shutter.photorize.domain.comment.service.CommentService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
		@RequestBody CommentCreateRequest commentCreateRequest,
		@AuthUser ContextMember contextMember) {
		return ApiResponse.created(commentService.createComment(contextMember.getId(), commentCreateRequest));
	}

	@PostMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId,
		@RequestBody CommentUpdateRequest commentUpdateRequest,
		@AuthUser ContextMember contextMember) {
		commentService.updateComment(contextMember.getId(), commentId, commentUpdateRequest);
		return ApiResponse.ok(null);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId,
		@AuthUser ContextMember contextMember) {
		commentService.deleteComment(contextMember.getId(), commentId);
		return ApiResponse.ok(null);
	}
}
