package com.shutter.photorize.domain.alarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

	PUBLIC("초대장이 도착했어요.", "초대: {sender}"),
	PRIVATE("일기가 도착했어요.", "보낸이: {sender}");

	private final String title;
	private final String message;

	public String getFormattedMessage(String sender) {
		return message.replace("{sender}", sender);
	}
}
