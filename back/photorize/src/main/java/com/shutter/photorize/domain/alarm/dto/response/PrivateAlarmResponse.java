package com.shutter.photorize.domain.alarm.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.alarm.entity.InviteAlarm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PrivateAlarmResponse extends InviteAlarmResponse {
	private final String sender;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private final LocalDateTime date;

	@Builder
	private PrivateAlarmResponse(Long alarmId, AlarmType type, String url, String sender, LocalDateTime date) {
		super(alarmId, type, url);
		this.sender = sender;
		this.date = date;
	}

	public static PrivateAlarmResponse from(InviteAlarm inviteAlarm) {
		return PrivateAlarmResponse.builder()
			.alarmId(inviteAlarm.getId())
			.type(inviteAlarm.getType())
			.url(inviteAlarm.getSender().getImg())
			.sender(inviteAlarm.getMember().getNickname())
			.date(inviteAlarm.getMemory().getDate())
			.build();
	}
}
