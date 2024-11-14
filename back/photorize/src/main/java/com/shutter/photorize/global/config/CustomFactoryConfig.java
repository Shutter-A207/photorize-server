package com.shutter.photorize.global.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.member.strategy.EmailCodeStrategy;
import com.shutter.photorize.domain.member.strategy.EmailCodeType;
import com.shutter.photorize.domain.member.strategy.PasswordEmailStrategy;
import com.shutter.photorize.domain.member.strategy.SignInEmailStrategy;
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
	public Map<EmailCodeType, EmailCodeStrategy> authCodeStrategyMap(
		PasswordEmailStrategy passwordAuthStrategy,
		SignInEmailStrategy singInAuthStrategy
	) {
		Map<EmailCodeType, EmailCodeStrategy> authCodeStrategyMap = new ConcurrentHashMap<>();
		authCodeStrategyMap.put(EmailCodeType.SIGNUP, singInAuthStrategy);
		authCodeStrategyMap.put(EmailCodeType.PASSWORD_CHANGE, passwordAuthStrategy);
		return authCodeStrategyMap;

	}

	@Bean
	public AuthCodeEmailFormFactory authCodeEmailFormFactory(ResourceUtil resourceUtil) {
		return new AuthCodeEmailFormFactory(resourceUtil);
	}

	@Bean
	public SignInEmailStrategy singInAuthStrategy(
		RedisAuthCodeAdapter redisAuthCodeAdapter, MemberRepository memberRepository) {
		return new SignInEmailStrategy(redisAuthCodeAdapter, memberRepository);
	}

	@Bean
	public PasswordEmailStrategy passwordAuthStrategy(
		RedisAuthCodeAdapter redisAuthCodeAdapter,
		MemberRepository memberRepository
	) {
		return new PasswordEmailStrategy(redisAuthCodeAdapter, memberRepository);
	}

}
