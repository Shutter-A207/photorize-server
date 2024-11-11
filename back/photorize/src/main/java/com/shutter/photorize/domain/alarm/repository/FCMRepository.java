package com.shutter.photorize.domain.alarm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.alarm.entity.FCMToken;
import com.shutter.photorize.domain.member.entity.Member;

@Repository
public interface FCMRepository extends JpaRepository<FCMToken, Long> {

	List<FCMToken> findByMember(Member member);
}
