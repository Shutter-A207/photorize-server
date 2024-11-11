package com.shutter.photorize.domain.alarm.dto.response;

import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.alarm.entity.InviteAlarm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PublicAlarmResponse extends InviteAlarmResponse {
	private final String inviter;
	private final String albumName;

	@Builder
	private PublicAlarmResponse(Long alarmId, AlarmType type, String inviter,
		String albumName) {
		super(alarmId, type);
		this.inviter = inviter;
		this.albumName = albumName;
	}

	public static PublicAlarmResponse from(InviteAlarm inviteAlarm) {
		return PublicAlarmResponse.builder()
			.alarmId(inviteAlarm.getId())
			.type(inviteAlarm.getType())
			.inviter(inviteAlarm.getAlbum().getMember().getNickname())
			.albumName(inviteAlarm.getAlbum().getName())
			.build();
	}
}
