package com.shutter.photorize.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

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

		Member member = joinRequest.toMember(password);
		memberRepository.save(member);

		return member.getId();

	}

	// public JwtDto login(SigninRequest signinRequest) {
	// 	Member member = memberRepository.getOrThrow(signinRequest.getEmail());
	//
	// 	matchPassword(signinRequest.getPassword(), member.getPassword());
	//
	// 	String accessToken = jwtUtil.createAccessToken(member.getEmail());
	// 	String refreshToken = jwtUtil.createRefreshToken(member.getEmail());
	//
	// 	return JwtDto.of(accessToken, refreshToken);
	// }

	private void matchPassword(String plain, String encryptedPassword) {
		if (!passwordEncoder.matches(plain, encryptedPassword)) {
			throw new PhotorizeException(ErrorType.MISMATCH_PASSWORD);
		}
	}
}
