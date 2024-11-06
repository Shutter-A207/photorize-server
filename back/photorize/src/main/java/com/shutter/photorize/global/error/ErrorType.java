package com.shutter.photorize.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

	// global 에러
	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 예상치 못한 오류가 발생했습니다."),
	NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	// member
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "패스워드가 일치하기 않습니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임이 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다."),

	// album
	NO_ALLOCATED_ALBUM(HttpStatus.FORBIDDEN, "해당 앨범 멤버가 아닙니다."),
	NO_ALBUM_TYPE_FOUND(HttpStatus.NOT_FOUND, "앨범 타입을 찾을 수 없습니다."),
	NO_ALBUM_FOUND(HttpStatus.NOT_FOUND, "앨범을 찾을 수 없습니다."),
	NO_COLOR_FOUND(HttpStatus.NOT_FOUND, "컬러를 찾을 수 없습니다."),

	// spot
	SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지점을 찾을 수 없습니다."),

	// pose
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),
	ALREADY_DISLIKED(HttpStatus.BAD_REQUEST, "좋아요가 취소된 상태입니다."),
	NO_POSE_FOUND(HttpStatus.NOT_FOUND, "포즈를 찾을 수 없습니다."),

	// memory
	MEMORY_NOT_FOUND(HttpStatus.NOT_FOUND, "추억을 찾을 수 없습니다."),

	// file
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 파일을 찾을 수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
	FILE_UNSUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다."),
	INVALID_S3_URL(HttpStatus.BAD_REQUEST, "잘못된 S3 URL입니다."),

	// comment
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
	COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글 작성자와 일치하지 않습니다."),
	; // 커스텀 에러 작성

	private final HttpStatus status;
	private final String message;
}
