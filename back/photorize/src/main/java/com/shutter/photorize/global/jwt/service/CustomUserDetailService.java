package com.shutter.photorize.global.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.user.model.entity.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberRepository.getOrThrow(username);

		return new CustomUserDetails(member);
	}
}
