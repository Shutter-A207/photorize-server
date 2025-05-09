package com.shutter.photorize.domain.alarm.repository;

import com.shutter.photorize.domain.alarm.entity.InviteAlarm;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    default InviteAlarm getByMemberIdAndAlbumIdOrThrow(Long memberId, Long albumId) {
        return findByMemberIdAndAlbumId(memberId, albumId).orElseThrow(
                () -> new PhotorizeException(ErrorType.ALARM_NOT_FOUND));
    }

    @Query("SELECT ia FROM InviteAlarm ia " +
            "JOIN FETCH ia.member " +
            "LEFT JOIN FETCH ia.album album " +
            "LEFT JOIN FETCH album.member " +
            "LEFT JOIN FETCH ia.memory memory " +
            "WHERE ia.member = :member " +
            "ORDER BY ia.updatedAt DESC")
    Slice<InviteAlarm> findByMember(@Param("member") Member member, Pageable pageable);

    @Query("SELECT ia FROM InviteAlarm ia " +
            "LEFT JOIN FETCH ia.member " +
            "LEFT JOIN FETCH ia.album " +
            "LEFT JOIN FETCH ia.memory " +
            "WHERE ia.id = :alarmId")
    Optional<InviteAlarm> findByIdWithMemberAndAlbumAndMemory(@Param("alarmId") Long alarmId);

    @Query(""" 
            SELECT ia
            FROM InviteAlarm ia
            LEFT JOIN FETCH ia.memory m
            LEFT JOIN FETCH ia.album al
            WHERE ia.id = :alarmId
            """)
    Optional<InviteAlarm> findAlarmDetailsById(@Param("alarmId") Long alarmId);

    @Query(value = "SELECT * FROM invite_alarm WHERE member_id = :memberId AND album_id = :albumId", nativeQuery = true)
    Optional<InviteAlarm> findByMemberIdAndAlbumId(@Param("memberId") Long memberId, @Param("albumId") Long albumId);
}
