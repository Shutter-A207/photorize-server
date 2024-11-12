package com.shutter.photorize.domain.alarm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmRequest;
import com.shutter.photorize.domain.alarm.dto.response.InviteAlarmResponse;
import com.shutter.photorize.domain.alarm.dto.response.PrivateAlarmResponse;
import com.shutter.photorize.domain.alarm.dto.response.PublicAlarmResponse;
import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.alarm.entity.InviteAlarm;
import com.shutter.photorize.domain.alarm.repository.InviteAlarmRepository;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.album.entity.AlbumType;
import com.shutter.photorize.domain.album.repository.AlbumMemberListRepository;
import com.shutter.photorize.domain.album.repository.AlbumRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteAlarmService {

	private final InviteAlarmRepository inviteAlarmRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;
	private final MemoryRepository memoryRepository;
	private final AlbumMemberListRepository albumMemberListRepository;
	private final FCMService fcmService;

	public SliceResponse<InviteAlarmResponse> getInviteAlarms(Long memberId, Pageable pageable) {
		Member member = memberRepository.getOrThrow(memberId);
		Slice<InviteAlarmResponse> inviteAlarms = inviteAlarmRepository.findByMember(member, pageable)
			.map(this::toInviteAlarmResponse);

		return SliceResponse.of(inviteAlarms);
	}

	@Transactional
	public void sendPrivateAlarm(List<Long> albumIds, Memory memory, Member sender) {
		albumIds.forEach(albumId -> {
			Album album = albumRepository.getOrThrow(albumId);
			Member member = album.getMember();
			InviteAlarm inviteAlarm = InviteAlarm.of(member, sender, memory);
			inviteAlarmRepository.save(inviteAlarm);
			fcmService.sendPrivateAlarm(member, memory.getMember());
		});
	}

	@Transactional
	public void sendPublicAlarm(List<Long> memberIds, Album album, Member sender) {
		memberIds.forEach(memberId -> {
			Member member = memberRepository.getOrThrow(memberId);
			InviteAlarm inviteAlarm = InviteAlarm.of(member, sender, album);
			inviteAlarmRepository.save(inviteAlarm);
			fcmService.sendPublicAlarm(member, album.getMember());
		});
	}

	@Transactional
	public void respondToInviteAlarm(Long memberId, Long alarmId, InviteAlarmRequest inviteAlarmRequest) {
		if (inviteAlarmRequest.isAccepted()) {
			acceptInviteAlarm(memberId, alarmId);
			return;
		}

		refuseInviteAlarm(alarmId);
	}

	private void acceptInviteAlarm(Long memberId, Long alarmId) {
		Member member = memberRepository.getOrThrow(memberId);
		InviteAlarm inviteAlarm = inviteAlarmRepository.getByIdWithMemberAndAlbumAndMemoryOrThrow(alarmId);

		if (inviteAlarm.getType() == AlarmType.PRIVATE) {
			Album memberPrivateAlbum = getOrCreatePrivateAlbumForMember(member);
			Memory memory = inviteAlarm.getMemory();
			Memory copiedMemory = Memory.of(member, memberPrivateAlbum, memory);
			memoryRepository.save(copiedMemory);
		} else if (inviteAlarm.getType() == AlarmType.PUBLIC) {
			// 공유 앨범에 멤버 추가
			Album publicAlbum = inviteAlarm.getAlbum();
			updateMemberStatusPublicAlbum(publicAlbum, member);
		}
		inviteAlarm.softDelete();
	}

	private void refuseInviteAlarm(Long alarmId) {
		InviteAlarm inviteAlarm = inviteAlarmRepository.getByIdOrThrow(alarmId);
		inviteAlarm.softDelete();
	}

	private Album getOrCreatePrivateAlbumForMember(Member member) {
		return albumRepository.findByMemberAndType(member, AlbumType.PRIVATE)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALBUM_FOUND));
	}

	private void updateMemberStatusPublicAlbum(Album album, Member member) {
		AlbumMemberList albumMemberList = albumMemberListRepository.findByAlbumAndMember(album, member)
			.orElseThrow(() -> new PhotorizeException(ErrorType.NO_ALLOCATED_ALBUM));

		albumMemberList.updateStatus(true);
	}

	private InviteAlarmResponse toInviteAlarmResponse(InviteAlarm inviteAlarm) {
		if (inviteAlarm.getType() == AlarmType.PUBLIC) {
			return PublicAlarmResponse.from(inviteAlarm);
		} else if (inviteAlarm.getType() == AlarmType.PRIVATE) {
			return PrivateAlarmResponse.from(inviteAlarm);
		}
		return null;
	}

}
