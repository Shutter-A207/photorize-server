package com.shutter.photorize.domain.alarm.dto.response;

import com.shutter.photorize.domain.alarm.entity.AlarmType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteAlarmResponse {
	private Long alarmId;
	private AlarmType type;
}
