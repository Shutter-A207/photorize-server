package com.shutter.photorize.domain.member.service;

import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.CustomOAuthUser;
import com.shutter.photorize.domain.member.dto.response.KakaoResponse;
import com.shutter.photorize.domain.member.dto.response.OAuth2Response;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.entity.ProviderType;
import com.shutter.photorize.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;
	private final FileService fileService;
	private final MemberService memberService;
	private final AlbumService albumService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		log.debug("loadUser : {}", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response;
		if (registrationId.equals(ProviderType.KAKAO.type())) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		Member member = insertMember(oAuth2Response);
		albumService.createPrivateAlbum(member.getId());
		log.debug("member info: {}, {}, {}", member.getId(), member.getEmail(), member.getProvider());

		return CustomOAuthUser.of(member.getId(), member.getEmail(), member.getProvider());

	}

	@Transactional
	protected Member insertMember(OAuth2Response oAuth2Response) {

		String defaultImg = fileService.getDefaultProfile();

		String emailPrefix = oAuth2Response.getEmail().split("@")[0];
		String randomStr = UUID.randomUUID().toString().substring(0, 2);
		String nickname = emailPrefix + "_" + randomStr;

		return memberRepository.findByEmail(oAuth2Response.getEmail())
			.orElseGet(() -> memberRepository.save(
				Member.of(oAuth2Response.getEmail(), nickname, null, defaultImg, oAuth2Response.getProviderType())
			));
	}
}
