package com.shutter.photorize.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

	// global 에러 (00)
	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "00-001", "현재 앱에 문제가 발생했으니 관리자에게 문의해주세요."),
	NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "00-002", "요청한 리소스를 찾을 수 없습니다."),

	// member (01)
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "01-001", "사용자를 찾을 수 없습니다."),
	MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "01-002", "패스워드가 일치하지 않습니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "01-003", "중복된 닉네임이 존재합니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "01-004", "중복된 이메일이 존재합니다."),
	EXPIRED_EMAIL_CODE(HttpStatus.BAD_REQUEST, "01-005", "인증 코드의 유효시간이 경과했습니다. 다시 시도해주세요."),
	INVALID_EMAIL_VERIFIED(HttpStatus.BAD_REQUEST, "01-006", "인증되지 않은 이메일입니다."),
	INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, "01-007", "인증 코드가 올바르지 않습니다. 다시 시도해주세요"),
	OAUTH2_AUTHENTICATION_FAILED(HttpStatus.FORBIDDEN, "01-008", "OAuth2 인증 과정에 실패했습니다."),
	EMAIL_IN_PROGRESS(HttpStatus.BAD_REQUEST, "01-009", "이미 인증요청을 한 이메일 입니다. 잠시 후 다시 시도해주세요."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "01-010", "만료된 리프레시토큰입니다. 재로그인을 해주세요."),
	INVALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "01-011", "리프레시토큰이 일치하지않습니다."),
	TOKEN_REISSUE_FAILED(HttpStatus.FORBIDDEN, "01-012", "어세스토큰 재발급이 실패했습니다."),
	KAKAO_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "01-013", "이미 카카오로 가입된 이메일입니다."),
	BASIC_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "01-014", "이미 기본회원가입으로 가입된 이메일입니다."),

	// album (02)
	NO_ALLOCATED_ALBUM(HttpStatus.FORBIDDEN, "02-001", "해당 앨범 멤버가 아닙니다."),
	NO_ALBUM_TYPE_FOUND(HttpStatus.NOT_FOUND, "02-002", "앨범 타입을 찾을 수 없습니다."),
	NO_ALBUM_FOUND(HttpStatus.NOT_FOUND, "02-003", "앨범을 찾을 수 없습니다."),
	NO_COLOR_FOUND(HttpStatus.NOT_FOUND, "02-004", "컬러를 찾을 수 없습니다."),

	// spot (03)
	SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "03-001", "해당 지점을 찾을 수 없습니다."),

	// pose (04)
	ALREADY_LIKED(HttpStatus.BAD_REQUEST, "04-001", "이미 좋아요를 눌렀습니다."),
	ALREADY_DISLIKED(HttpStatus.BAD_REQUEST, "04-002", "좋아요가 취소된 상태입니다."),
	NO_POSE_FOUND(HttpStatus.NOT_FOUND, "04-003", "포즈를 찾을 수 없습니다."),

	// memory (05)
	MEMORY_NOT_FOUND(HttpStatus.NOT_FOUND, "05-001", "추억을 찾을 수 없습니다."),
	MEMORY_FORBIDDEN(HttpStatus.NOT_FOUND, "05-002", "추억 권한이 없습니다."),

	// file (06)
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "06-001", "요청한 파일을 찾을 수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "06-002", "파일 업로드 중 오류가 발생했습니다."),
	FILE_UNSUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "06-003", "지원하지 않는 확장자입니다."),
	INVALID_S3_URL(HttpStatus.BAD_REQUEST, "06-004", "잘못된 S3 URL입니다."),

	// comment (07)
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "07-001", "댓글을 찾을 수 없습니다."),
	COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "07-002", "댓글 작성자와 일치하지 않습니다."),

	// alarm (08)
	ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "08-001", "요청한 알림을 찾을 수 없습니다."),
	; // 커스텀 에러 작성

	private final HttpStatus status;
	private final String errorCode;
	private final String message;
}
