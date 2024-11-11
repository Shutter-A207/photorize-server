package com.shutter.photorize.domain.alarm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmRequest;
import com.shutter.photorize.domain.alarm.dto.response.InviteAlarmResponse;
import com.shutter.photorize.domain.alarm.service.InviteAlarmService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class InviteAlarmController {

	private final InviteAlarmService inviteAlarmService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<InviteAlarmResponse>>> getInviteAlarms(
		@AuthUser ContextMember contextMember) {
		List<InviteAlarmResponse> alarms = inviteAlarmService.getInviteAlarms(contextMember.getId());
		return ApiResponse.ok(alarms);
	}

	@PostMapping("{alarmId}")
	public ResponseEntity<ApiResponse<Void>> respondToInviteAlarm(
		@PathVariable Long alarmId,
		@RequestBody InviteAlarmRequest inviteAlarmRequest,
		@AuthUser ContextMember contextMember) {

		inviteAlarmService.respondToInviteAlarm(contextMember.getId(), alarmId, inviteAlarmRequest);
		return ApiResponse.ok(null);
	}
}
