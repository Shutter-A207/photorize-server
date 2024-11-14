package com.shutter.photorize.domain.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.shutter.photorize.domain.alarm.dto.request.FCMTokenRequest;
import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.alarm.entity.FCMToken;
import com.shutter.photorize.domain.alarm.repository.FCMRepository;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

	private final FCMRepository fcmRepository;
	private final MemberRepository memberRepository;
	private final FirebaseMessaging firebaseMessaging;

	@Transactional
	public void saveToken(Long memberId, FCMTokenRequest fcmTokenRequest) {
		Member member = memberRepository.getOrThrow(memberId);
		fcmRepository.findByToken(fcmTokenRequest.getToken())
			.ifPresentOrElse(
				existingToken -> existingToken.updateMember(member),
				() -> fcmRepository.save(fcmTokenRequest.from(member)));
	}

	@Transactional
	public void deleteToken(Long memberId, FCMTokenRequest fcmTokenRequest) {
		Member member = memberRepository.getOrThrow(memberId);
		fcmRepository.findByTokenAndMember(fcmTokenRequest.getToken(), member)
			.ifPresent(fcmRepository::delete);
	}

	public void sendPublicAlarm(Member member, Member createMember) {
		List<FCMToken> fcmTokens = fcmRepository.findByMember(member);

		fcmTokens.forEach(fcmToken ->
			sendAlarm(fcmToken, AlarmType.PUBLIC, createMember)
		);
	}

	public void sendPrivateAlarm(Member member, Member writerMember) {
		List<FCMToken> fcmTokens = fcmRepository.findByMember(member);

		fcmTokens.forEach(fcmToken ->
			sendAlarm(fcmToken, AlarmType.PRIVATE, writerMember)
		);
	}

	private void sendAlarm(FCMToken fcmToken, AlarmType alarmType, Member writerMember) {
		Notification notification = makeNotification(alarmType, writerMember);
		try {
			firebaseMessaging.send(makeMessage(notification, fcmToken));
		} catch (FirebaseMessagingException e) {
			if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
				fcmRepository.delete(fcmToken);
				log.warn("{}", e.getMessage());
			} else if (e.getMessagingErrorCode().equals(MessagingErrorCode.INVALID_ARGUMENT)) {
				fcmRepository.delete(fcmToken);
				log.warn("{}", e.getMessage());
			} else
				throw new RuntimeException(e);
		}
	}

	private Notification makeNotification(AlarmType type, Member member) {
		return Notification.builder()
			.setTitle(type.getTitle())
			.setBody(type.getFormattedMessage(member.getNickname()))
			.build();
	}

	private Message makeMessage(Notification notification, FCMToken fcmToken) {
		return Message.builder()
			.setToken(fcmToken.getToken())
			.setNotification(notification)
			.build();
	}
}
