package com.shutter.photorize.global.jwt.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final SecretKey secretKey;
	private long expireAccessToken;
	private long expireRefreshToken;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret,
		@Value("${spring.jwt.expire.time.access}") long expireAccessToken,
		@Value("${spring.jwt.expire.time.refresh}") long expireRefreshToken) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
		this.expireAccessToken = expireAccessToken;
		this.expireRefreshToken = expireRefreshToken;
	}

	public String createAccessToken(String username) {
		return Jwts.builder()
			.claim("username", username)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expireAccessToken))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(String username) {
		return Jwts.builder()
			.claim("username", username)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expireRefreshToken))
			.signWith(secretKey)
			.compact();
	}

	// 페이로드 부분에서 "username"키에 해당하는 값(이메일) 추출
	public String getEmail(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token.replace(" ", "")).getPayload()
			.get("username", String.class);
	}

	public Boolean validation(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("만료된 토큰입니다.");
		}

		return false;
	}
}
