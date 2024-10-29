package com.shutter.photorize.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PhotorizeException.class)
	public ResponseEntity<ApiResponse<Void>> handlePhotorizeException(PhotorizeException ex) {
		ErrorType errorType = ex.getErrorType();
		return ApiResponse.error(errorType.getStatus(), errorType.getMessage());
	}
}