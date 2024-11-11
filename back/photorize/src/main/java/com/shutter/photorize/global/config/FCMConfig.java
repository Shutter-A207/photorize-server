package com.shutter.photorize.global.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

@Configuration
public class FCMConfig {

	@Value("${spring.fcm.certification-path}")
	private String googleApplicationCredentials;

	@PostConstruct
	public void init() throws IOException {
		ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);
		try (InputStream in = resource.getInputStream()) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(in))
				.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		}
	}

	@Bean
	public FirebaseMessaging firebaseMessaging() {
		return FirebaseMessaging.getInstance();
	}
}
