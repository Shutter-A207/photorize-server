package com.shutter.photorize.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	default Member getOrThrow(String email) {
		return findByEmail(email).orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
	}

	default Member getOrThrow(Long id) {
		return findById(id).orElseThrow(() -> new PhotorizeException(ErrorType.USER_NOT_FOUND));
	}

	Optional<Member> findByEmail(String email);

	Boolean existsByUsername(String username);
}
