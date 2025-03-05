package com.shutter.photorize.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean(name = "fileUploadExecutor")
	public Executor fileUploadExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setThreadGroupName("fileUploadExecutor-");
		executor.setCorePoolSize(10); //최소 스레드 수
		executor.setMaxPoolSize(20); // 최대 스레드 수
		executor.setQueueCapacity(50); //대기 큐 용량
		executor.initialize();

		return executor;
	}

	@Bean(name = "mailSendExecutor")
	public Executor mailExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setThreadGroupName("mailSendExecutor-");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(20);
		executor.initialize();
		return executor;
	}
}