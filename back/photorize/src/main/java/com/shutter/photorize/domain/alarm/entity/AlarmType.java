package com.shutter.photorize.domain.alarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

	PUBLIC("앨범 초대장이 도착했어요.", "초대: {sender}"),
	PRIVATE("추억이 도착했어요.", "보낸이: {sender}");

	private final String title;
	private final String message;

	public String getFormattedMessage(String sender) {
		return message.replace("{sender}", sender);
	}
}
