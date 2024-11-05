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
import com.shutter.photorize.domain.comment.service.CommentService;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
		//TODO: 하드 코딩 수정해야합니다.
		commentService.createComment(commentCreateRequest, 1L);
		return ApiResponse.created();
	}

	@PostMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId,
		@RequestBody CommentUpdateRequest commentUpdateRequest) {
		commentService.updateComment(commentId, commentUpdateRequest, 1L);
		return ApiResponse.ok(null);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId, 1L);
		return ApiResponse.ok(null);
	}
}
