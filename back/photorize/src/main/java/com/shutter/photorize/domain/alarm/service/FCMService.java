package com.shutter.photorize.domain.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.shutter.photorize.domain.alarm.dto.request.FCMTokenSaveRequest;
import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.alarm.entity.FCMToken;
import com.shutter.photorize.domain.alarm.repository.FCMRepository;
import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FCMService {

	private final FCMRepository fcmRepository;
	private final MemberRepository memberRepository;
	private final FirebaseMessaging firebaseMessaging;

	public void saveToken(Long memberId, FCMTokenSaveRequest fcmTokenSaveRequest) {
		Member member = memberRepository.getOrThrow(memberId);
		fcmRepository.save(fcmTokenSaveRequest.from(member));
	}

	public void sendPublicAlarm(Member member, Album album) {
		List<FCMToken> fcmTokens = fcmRepository.findByMember(member);

		fcmTokens.forEach(fcmToken ->
			sendAlarm(fcmToken.getToken(), AlarmType.PUBLIC, album.getMember())
		);
	}

	public void sendPrivateAlarm(Member member, Album album) {
		List<FCMToken> fcmTokens = fcmRepository.findByMember(member);

		fcmTokens.forEach(fcmToken ->
			sendAlarm(fcmToken.getToken(), AlarmType.PRIVATE, album.getMember())
		);
	}

	private void sendAlarm(String token, AlarmType alarmType, Member member) {
		Notification notification = makeNotification(alarmType, member);
		try {
			firebaseMessaging.send(makeMessage(notification, token));
		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Notification makeNotification(AlarmType type, Member member) {
		return Notification.builder()
			.setTitle(type.getTitle())
			.setBody(type.getFormattedMessage(member.getNickname()))
			.build();
	}

	private Message makeMessage(Notification notification, String token) {
		return Message.builder()
			.setToken(token)
			.setNotification(notification)
			.build();
	}
}
