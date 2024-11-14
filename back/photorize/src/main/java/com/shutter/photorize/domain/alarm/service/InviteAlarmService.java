package com.shutter.photorize.domain.alarm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmRequest;
import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmResendRequest;
import com.shutter.photorize.domain.alarm.dto.response.InviteAlarmDetailResponse;
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
import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.file.entity.FileType;
import com.shutter.photorize.domain.file.repository.FileRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.domain.memory.repository.MemoryRepository;
import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;
import com.shutter.photorize.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteAlarmService {

	private final InviteAlarmRepository inviteAlarmRepository;
	private final MemberRepository memberRepository;
	private final AlbumRepository albumRepository;
	private final MemoryRepository memoryRepository;
	private final AlbumMemberListRepository albumMemberListRepository;
	private final FileRepository fileRepository;
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
		log.info("조건문 타기전: {}", inviteAlarm.getType());
		if (inviteAlarm.getType() == AlarmType.PRIVATE) {
			log.info("PRIVATE일 경우: {}", inviteAlarm.getType());
			Album memberPrivateAlbum = getOrCreatePrivateAlbumForMember(member);
			Memory memory = inviteAlarm.getMemory();
			Memory copiedMemory = Memory.of(member, memberPrivateAlbum, memory);
			memoryRepository.save(copiedMemory);
			copyAndSaveFilesForMemory(memory, copiedMemory);
		} else if (inviteAlarm.getType() == AlarmType.PUBLIC) {
			// 공유 앨범에 멤버 추가
			log.info("PUBLIC일 경우: {}", inviteAlarm.getType());
			Album publicAlbum = inviteAlarm.getAlbum();
			updateMemberStatusPublicAlbum(publicAlbum, member);
		}
		inviteAlarm.softDelete();
	}

	public InviteAlarmDetailResponse getInviteAlarmDetail(Long memberId, Long alarmId) {
		InviteAlarm inviteAlarm = inviteAlarmRepository.findAlarmDetailsById(alarmId)
			.orElseThrow(() -> new PhotorizeException(ErrorType.ALARM_NOT_FOUND));

		return switch (inviteAlarm.getType()) {
			case PRIVATE -> createPrivateAlarmResponse(inviteAlarm);
			case PUBLIC -> createPublicAlarmResponse(inviteAlarm);
		};
	}

	@Transactional
	public void resendInviteAlarm(InviteAlarmResendRequest inviteAlarmResendRequest) {
		//TODO: 메서드 분리 및 리팩토링 수행할 것
		Member member = memberRepository.getOrThrow(inviteAlarmResendRequest.getMemberId());
		Album album = albumRepository.getOrThrow(inviteAlarmResendRequest.getAlbumId());
		//회원의 수락 여부를 확인해야한다.
		AlbumMemberList albumMemberList = albumMemberListRepository.getByAlbumAndMemberOrThrow(album, member);

		if (albumMemberList.isStatus()) {
			return;
		}

		//TODO: 재전송 로직 구현
		//아니라면 재전송을 한다.
		InviteAlarm inviteAlarm = inviteAlarmRepository.getByMemberIdAndAlbumIdOrThrow(
			inviteAlarmResendRequest.getMemberId(), inviteAlarmResendRequest.getAlbumId());
		//회원이 응답을 할경우
		if (inviteAlarm.isDeleted()) {
			inviteAlarm.restore();
		}

	}

	private InviteAlarmDetailResponse createPrivateAlarmResponse(InviteAlarm inviteAlarm) {
		File file = fileRepository.getFilesByMemoryAndTypeOrThrow(
			inviteAlarm.getMemory(), FileType.PHOTO
		);
		return InviteAlarmDetailResponse.of(inviteAlarm.getType(), inviteAlarm.getMemory(), file);
	}

	private InviteAlarmDetailResponse createPublicAlarmResponse(InviteAlarm inviteAlarm) {
		List<AlbumMemberList> albumMemberLists = albumMemberListRepository.findMembersByAlbum(
			inviteAlarm.getAlbum()
		);
		return InviteAlarmDetailResponse.of(inviteAlarm.getType(), albumMemberLists);
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

	private void copyAndSaveFilesForMemory(Memory originalMemory, Memory copiedMemory) {
		List<File> filesByMemory = fileRepository.findFilesByMemory(originalMemory);

		for (File file : filesByMemory) {
			File copiedFile = File.of(copiedMemory, file.getType(), file.getUrl());
			fileRepository.save(copiedFile);
		}
	}
}
