package com.shutter.photorize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotorizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotorizeApplication.class, args);
	}

}
