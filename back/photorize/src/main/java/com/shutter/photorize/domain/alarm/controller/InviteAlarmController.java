package com.shutter.photorize.domain.alarm.controller;

import static com.shutter.photorize.global.constant.CommonConstants.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmRequest;
import com.shutter.photorize.domain.alarm.dto.request.InviteAlarmResendRequest;
import com.shutter.photorize.domain.alarm.dto.response.InviteAlarmDetailResponse;
import com.shutter.photorize.domain.alarm.dto.response.InviteAlarmResponse;
import com.shutter.photorize.domain.alarm.service.InviteAlarmService;
import com.shutter.photorize.global.jwt.model.ContextMember;
import com.shutter.photorize.global.response.ApiResponse;
import com.shutter.photorize.global.response.SliceResponse;
import com.shutter.photorize.global.security.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class InviteAlarmController {

	private final InviteAlarmService inviteAlarmService;

	@GetMapping
	public ResponseEntity<ApiResponse<SliceResponse<InviteAlarmResponse>>> getInviteAlarms(
		@AuthUser ContextMember contextMember,
		@RequestParam(defaultValue = "0") int pageNumber) {

		Pageable pageable = PageRequest.of(pageNumber, ALARM_PAGE_SIZE);

		return ApiResponse.ok(inviteAlarmService.getInviteAlarms(contextMember.getId(), pageable));
	}

	@PostMapping("{alarmId}")
	public ResponseEntity<ApiResponse<Void>> respondToInviteAlarm(
		@PathVariable Long alarmId,
		@RequestBody InviteAlarmRequest inviteAlarmRequest,
		@AuthUser ContextMember contextMember) {

		inviteAlarmService.respondToInviteAlarm(contextMember.getId(), alarmId, inviteAlarmRequest);
		return ApiResponse.ok(null);
	}

	@GetMapping("{alarmId}/detail")
	public ResponseEntity<ApiResponse<InviteAlarmDetailResponse>> getInviteAlarmDetail(
		@PathVariable Long alarmId,
		@AuthUser ContextMember contextMember) {
		return ApiResponse.ok(inviteAlarmService.getInviteAlarmDetail(contextMember.getId(), alarmId));
	}

	@PostMapping("/resend")
	public ResponseEntity<ApiResponse<Void>> resendInviteAlarm(
		@RequestBody InviteAlarmResendRequest inviteAlarmResendRequest) {
		inviteAlarmService.resendInviteAlarm(inviteAlarmResendRequest);
		return ApiResponse.created();
	}
}
