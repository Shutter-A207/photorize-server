package com.shutter.photorize.domain.alarm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteAlarmResendRequest {

	private Long albumId;
	private Long memberId;
}
