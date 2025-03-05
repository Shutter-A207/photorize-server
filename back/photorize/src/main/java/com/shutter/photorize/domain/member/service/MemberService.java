package com.shutter.photorize.domain.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.album.service.AlbumService;
import com.shutter.photorize.domain.file.entity.S3Folder;
import com.shutter.photorize.domain.file.service.FileService;
import com.shutter.photorize.domain.member.dto.LoginMemberProfileDto;
import com.shutter.photorize.domain.member.dto.MemberListDto;
import com.shutter.photorize.domain.member.dto.request.ChangePasswordRequest;
import com.shutter.photorize.domain.member.dto.request.JoinRequest;
import com.shutter.photorize.domain.member.dto.request.UpdateNicknameRequest;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.entity.ProviderType;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.member.strategy.EmailCodeType;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.util.S3Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final FileService fileService;
	private final AlbumRepository albumRepository;
	private final S3Utils s3Utils;
	private final EmailCodeService emailCodeService;
	private final AlbumService albumService;

	@Transactional

	public Long createMember(JoinRequest joinRequest) {

		validateDuplicateEmail(joinRequest.getEmail());
		checkNickname(joinRequest.getNickname());
		joinRequest.valid();

		String password = passwordEncoder.encode(joinRequest.getPassword());
		String defaultImg = fileService.getDefaultProfile();

		Member member = joinRequest.toMember(password, defaultImg, ProviderType.BASIC);
		memberRepository.save(member);

		Member savedMember = memberRepository.save(member);
		albumService.createPrivateAlbum(savedMember.getId());

		return savedMember.getId();
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

	@Transactional
	public LoginMemberProfileDto updateImg(Long memberId, MultipartFile file) {
		Member member = memberRepository.getOrThrow(memberId);
		String url = s3Utils.uploadProFile(file, S3Folder.PROFILE);

		member.updateProfile(url);

		LoginMemberProfileDto profileDto = LoginMemberProfileDto.of(member);

		return profileDto;
	}

	@Transactional
	public LoginMemberProfileDto updateNickname(Long memberId, UpdateNicknameRequest updateNicknameRequest) {
		Member member = memberRepository.getOrThrow(memberId);

		checkNickname(updateNicknameRequest.getNickname());

		member.updateNickname(updateNicknameRequest.getNickname());

		LoginMemberProfileDto profileDto = LoginMemberProfileDto.of(member);

		return profileDto;
	}

	public void checkNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new PhotorizeException(ErrorType.DUPLICATE_NICKNAME);
		}
	}

	public boolean validateNickname(String nickname) {
		return !memberRepository.existsByNickname(nickname);
	}

	@Transactional
	public boolean modifyPassword(ChangePasswordRequest changePasswordRequest) {
		emailCodeService.checkAvailable(changePasswordRequest.getEmail(), EmailCodeType.PASSWORD_CHANGE);

		changePasswordRequest.valid();

		Member member = memberRepository.getOrThrow(changePasswordRequest.getEmail());
		member.updatePassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
		return true;
	}

	public void validateDuplicateEmail(String email) {
		getProviderType(email).ifPresent(providerType -> {
			throw new PhotorizeException(
				providerType.equals(ProviderType.BASIC) ? ErrorType.BASIC_DUPLICATE_EMAIL :
					ErrorType.KAKAO_DUPLICATE_EMAIL
			);
		});
	}

	private Optional<ProviderType> getProviderType(String email) {
		return memberRepository.findByEmail(email).map(Member::getProvider);
	}

}
