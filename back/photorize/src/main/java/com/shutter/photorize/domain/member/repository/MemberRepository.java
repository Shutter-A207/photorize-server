package com.shutter.photorize.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	default Member getOrThrow(String email) {
		return findByEmail(email).orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
	}

	default Member getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
	}

	Optional<Member> findByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	List<Member> findByNicknameContainingAndIdNot(String keyword, Long memberId);

}
