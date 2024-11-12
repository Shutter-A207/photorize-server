package com.shutter.photorize.domain.member.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.KaKaoUserInfo;
import com.shutter.photorize.domain.member.dto.KakaoMemberDetails;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.entity.ProviderType;
import com.shutter.photorize.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoMemberDetailsService extends DefaultOAuth2UserService {

	private static final String PREFIX = "kakao ";

	private final MemberRepository memberRepository;
	private final FileService fileService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		KaKaoUserInfo kakaoUserInfo = new KaKaoUserInfo(oAuth2User.getAttributes());

		String defaultImg = fileService.getDefaultProfile();

		Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
			.orElseGet(() ->
				memberRepository.save(
					Member.of(kakaoUserInfo.getEmail(), PREFIX, null, defaultImg, ProviderType.KAKAO)
				));

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

		return new KakaoMemberDetails(String.valueOf(member.getEmail()), Collections.singletonList(authority),
			oAuth2User.getAttributes());
	}
}
