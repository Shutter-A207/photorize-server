package com.shutter.photorize.domain.alarm.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.alarm.entity.InviteAlarm;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

@Repository
public interface InviteAlarmRepository extends JpaRepository<InviteAlarm, Long> {

	default InviteAlarm getByIdOrThrow(Long id) {
		return findById(id).orElseThrow(
			() -> new PhotorizeException(ErrorType.ALARM_NOT_FOUND));
	}

	default InviteAlarm getByIdWithMemberAndAlbumAndMemoryOrThrow(Long id) {
		return findByIdWithMemberAndAlbumAndMemory(id).orElseThrow(
			() -> new PhotorizeException(ErrorType.ALARM_NOT_FOUND));
	}

	@Query("SELECT ia FROM InviteAlarm ia " +
		"JOIN FETCH ia.member " +
		"LEFT JOIN FETCH ia.album album " +
		"LEFT JOIN FETCH album.member " +
		"LEFT JOIN FETCH ia.memory memory " +
		"WHERE ia.member = :member " +
		"ORDER BY ia.createdAt DESC")
	Slice<InviteAlarm> findByMember(@Param("member") Member member, Pageable pageable);

	@Query("SELECT ia FROM InviteAlarm ia " +
		"LEFT JOIN FETCH ia.member " +
		"LEFT JOIN FETCH ia.album " +
		"LEFT JOIN FETCH ia.memory " +
		"WHERE ia.id = :alarmId")
	Optional<InviteAlarm> findByIdWithMemberAndAlbumAndMemory(@Param("alarmId") Long alarmId);
}
