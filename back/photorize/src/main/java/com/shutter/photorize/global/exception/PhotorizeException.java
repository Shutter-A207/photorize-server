package com.shutter.photorize.global.exception;

import com.shutter.photorize.global.error.ErrorType;

import lombok.Getter;

@Getter
public class PhotorizeException extends RuntimeException {

	private final ErrorType errorType;

	public PhotorizeException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
