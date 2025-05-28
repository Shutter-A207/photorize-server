package com.shutter.photorize.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExecutionLoggingAspect {

	@Around("execution(* com.shutter.photorize..repository..*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		String method = joinPoint.getSignature().toShortString();

		try {
			Object result = joinPoint.proceed();
			long time = System.currentTimeMillis() - start;
			log.info("✅[{}] - [{}] {} ms", MDC.get("traceId"), method, time);
			return result;
		} catch (Exception ex) {
			long time = System.currentTimeMillis() - start;
			log.warn("❌[{}] - [{}] {} ms", MDC.get("traceId"), method, time);
			throw ex;
		}
	}
}