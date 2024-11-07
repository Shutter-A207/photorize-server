package com.shutter.photorize.domain.member.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.LoginMemberProfileDto;
import com.shutter.photorize.domain.member.dto.MemberListDto;
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
	private final AlbumRepository albumRepository;

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

	public LoginMemberProfileDto getLoginMemberProfile(Long memberId) {
		return LoginMemberProfileDto.of(memberRepository.getOrThrow(memberId));
	}

	public List<MemberListDto> getMembers(String keyword, Long memberId) {
		Member member = memberRepository.getOrThrow(memberId);

		List<Member> results = memberRepository.findByNicknameContainingAndIdNot(keyword, member.getId());

		return results.stream()
			.map(result -> MemberListDto.from(
				result.getId(), result.getNickname(),
				albumRepository.findByMemberAndType(result, AlbumType.PRIVATE)
					.map(Album::getId)
					.orElse(null)
			))
			.toList();
	}

	public Boolean validateNickname(String nickname) {
		return !memberRepository.existsByNickname(nickname);
	}

}
