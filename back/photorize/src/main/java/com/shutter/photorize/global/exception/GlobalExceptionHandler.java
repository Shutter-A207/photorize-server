package com.shutter.photorize.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PhotorizeException.class)
	public ResponseEntity<ApiResponse<Void>> handlePhotorizeException(PhotorizeException ex) {
		ErrorType errorType = ex.getErrorType();
		log.error("PhotorizeException: {}", errorType.getMessage(), ex);
		return ApiResponse.error(errorType, errorType.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		log.error("Exception: {}", ex.getMessage(), ex);
		return ApiResponse.error(ErrorType.DEFAULT_ERROR, ErrorType.DEFAULT_ERROR.getMessage());
	}
}