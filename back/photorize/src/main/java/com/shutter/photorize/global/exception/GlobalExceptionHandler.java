package com.shutter.photorize.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PhotorizeException.class)
	public ResponseEntity<ApiResponse<Void>> handlePhotorizeException(PhotorizeException ex) {
		log.error("PhotorizeException: {}", ex.getMessage(), ex);
		return ApiResponse.error(ex.getErrorType());
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
		log.warn("NoResourceFoundException: {}", ex.getMessage(), ex);
		return ApiResponse.error(ErrorType.NO_RESOURCE_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		log.error("Exception: {}", ex.getMessage(), ex);
		return ApiResponse.error(ErrorType.DEFAULT_ERROR);
	}
}