package com.shutter.photorize.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.model.MemberDto;
import com.shutter.photorize.domain.member.model.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.memberRepository = memberRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void joinProcess(MemberDto memberDto) {
		String username = memberDto.getUsername();
		String password = memberDto.getPassword();

		Boolean isExist = memberRepository.existsByUsername(username);

		if (isExist) {
			return;
		}

		Member data = new Member();

		data.setUsername(username);
		data.setPassword(bCryptPasswordEncoder.encode(password));
		data.setRole("ROLE_MEMBER");

		memberRepository.save(data);
	}
}
