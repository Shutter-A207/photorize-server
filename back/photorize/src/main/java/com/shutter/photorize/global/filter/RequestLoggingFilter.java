package com.shutter.photorize.global.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

	private static final String MDC_TRACE_ID_KEY = "traceId";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;

		String traceId = UUID.randomUUID().toString();
		MDC.put(MDC_TRACE_ID_KEY, traceId);

		String method = httpRequest.getMethod();
		String uri = httpRequest.getRequestURI();

		long startTime = System.currentTimeMillis();
		log.info("➡️[{}] - [{}] {}", MDC.get("traceId"), method, uri);

		try {
			chain.doFilter(request, response);
		} finally {
			long totalTime = System.currentTimeMillis() - startTime;
			log.info("⬅️[{}] - [{}] {} - {} ms", traceId, method, uri, totalTime);
			MDC.remove(MDC_TRACE_ID_KEY);
		}
	}
}
