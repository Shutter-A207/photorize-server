package com.shutter.photorize.global.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.jwt.model.CustomUserDetails;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	// @Auth 존재 여부 확인
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new PhotorizeException(ErrorType.USER_NOT_FOUND);
		}

		Object principal = authentication.getPrincipal();
		if (!(principal instanceof CustomUserDetails userDetails)) {
			throw new PhotorizeException(ErrorType.USER_NOT_FOUND);
		}
		return userDetails.getMember();
	}
}
