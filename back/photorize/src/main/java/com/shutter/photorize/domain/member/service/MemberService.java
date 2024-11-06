package com.shutter.photorize.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.LoginMemberProfile;
import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final FileService fileService;

	@Transactional
	public Long createMember(JoinRequest joinRequest) {

		joinRequest.valid();

		if (memberRepository.existsByNickname(joinRequest.getNickname())) {
			throw new PhotorizeException(ErrorType.DUPLICATE_NICKNAME);
		}

		if (memberRepository.existsByEmail(joinRequest.getEmail())) {
			throw new PhotorizeException(ErrorType.DUPLICATE_EMAIL);
		}

		String password = passwordEncoder.encode(joinRequest.getPassword());
		String defaultImg = fileService.getDefaultProfile();

		Member member = joinRequest.toMember(password, defaultImg);
		memberRepository.save(member);

		return member.getId();

	}

	public LoginMemberProfile getLoginMemberProfile(Long memberId) {
		return LoginMemberProfile.of(memberRepository.getOrThrow(memberId));
	}

}
