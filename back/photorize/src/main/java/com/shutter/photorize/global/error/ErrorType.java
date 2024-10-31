package com.shutter.photorize.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 예상치 못한 오류가 발생했습니다."),
	NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	INVALID_ALBUM_TYPE(HttpStatus.NOT_FOUND, "앨범 타입을 찾을 수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
	NO_ALBUM_FOUND(HttpStatus.NOT_FOUND, "앨범을 찾을 수 없습니다."),
	; //커스텀 에러 작성

	private final HttpStatus status;
	private final String message;
}
