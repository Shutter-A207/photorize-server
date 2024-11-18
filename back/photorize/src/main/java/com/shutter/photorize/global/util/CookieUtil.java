package com.shutter.photorize.global.util;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieUtil {

	// HTTP 요청에서 특정 쿠키 이름 찾아서 반환
	static Optional<Cookie> resolveCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}

		return Optional.empty();

	}

	// 쿠키 삭제, maxAge를 0으로 설정해서 브라우저가 파기하도록 함.
	static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Optional<Cookie> optionalCookie = resolveCookie(request, name);
		if (optionalCookie.isPresent()) {
			Cookie cookie = optionalCookie.get();
			cookie.setValue("");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * @param response 응답에 쿠키를 적어서 보내줌
	 * @param cookieName key
	 * @param cookieContents value
	 * @param maxAge 초 단위
	 */

	// 새로운 쿠키 생성하고 설정.
	static void setCookie(HttpServletResponse response,
		String cookieName, String cookieContents, int maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieContents);
		cookie.setSecure(true); // HTTPS 에서만 전송
		cookie.setHttpOnly(false); // JavaScript에서 접근 불가
		cookie.setMaxAge(maxAge);
		cookie.setAttribute("SameSite", "None");
		cookie.setDomain("photorize.co.kr");
		cookie.setPath("/"); // 전체 도메인에서 접근 가능
		response.addCookie(cookie);
	}

}
