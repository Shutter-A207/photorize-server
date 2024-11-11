package com.shutter.photorize.global.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.member.strategy.AuthCodeStrategy;
import com.shutter.photorize.domain.member.strategy.AuthCodeType;
import com.shutter.photorize.domain.member.strategy.PasswordAuthStrategy;
import com.shutter.photorize.domain.member.strategy.SignInAuthStrategy;
import com.shutter.photorize.global.util.ResourceUtil;
import com.shutter.photorize.infra.mail.model.EmailFormType;
import com.shutter.photorize.infra.mail.service.AuthCodeEmailFormFactory;
import com.shutter.photorize.infra.mail.service.EmailFormFactory;
import com.shutter.photorize.infra.redis.service.RedisAuthCodeAdapter;

@Configuration
public class CustomFactoryConfig {

	@Bean
	public Map<EmailFormType, EmailFormFactory> emailFormFactoryMap(AuthCodeEmailFormFactory authCodeEmailFormFactory) {
		Map<EmailFormType, EmailFormFactory> emailFormFactoryMap = new ConcurrentHashMap<>();
		emailFormFactoryMap.put(EmailFormType.SIGNUP_AUTH, authCodeEmailFormFactory);
		emailFormFactoryMap.put(EmailFormType.PASSWORD_CHANGE_AUTH, authCodeEmailFormFactory);
		return emailFormFactoryMap;
	}

	@Bean
	public Map<AuthCodeType, AuthCodeStrategy> authCodeStrategyMap(
		PasswordAuthStrategy passwordAuthStrategy,
		SignInAuthStrategy singInAuthStrategy
	) {
		Map<AuthCodeType, AuthCodeStrategy> authCodeStrategyMap = new ConcurrentHashMap<>();
		authCodeStrategyMap.put(AuthCodeType.SIGNUP, singInAuthStrategy);
		authCodeStrategyMap.put(AuthCodeType.PASSWORD_CHANGE, passwordAuthStrategy);
		return authCodeStrategyMap;

	}

	@Bean
	public AuthCodeEmailFormFactory authCodeEmailFormFactory(ResourceUtil resourceUtil) {
		return new AuthCodeEmailFormFactory(resourceUtil);
	}

	@Bean
	public SignInAuthStrategy singInAuthStrategy(
		RedisAuthCodeAdapter redisAuthCodeAdapter, MemberRepository memberRepository) {
		return new SignInAuthStrategy(redisAuthCodeAdapter, memberRepository);
	}

	@Bean
	public PasswordAuthStrategy passwordAuthStrategy(
		RedisAuthCodeAdapter redisAuthCodeAdapter,
		MemberRepository memberRepository
	) {
		return new PasswordAuthStrategy(redisAuthCodeAdapter, memberRepository);
	}

}
