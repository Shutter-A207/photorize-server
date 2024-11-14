package com.shutter.photorize.domain.alarm.dto.response;

import java.util.List;

import com.shutter.photorize.domain.alarm.entity.AlarmType;
import com.shutter.photorize.domain.album.dto.response.AlbumMemberListResponse;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.memory.entity.Memory;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteAlarmDetailResponse {

	private AlarmType alarmType;
	private String url;
	private String content;
	private List<AlbumMemberListResponse> memberList;

	@Builder
	public InviteAlarmDetailResponse(AlarmType alarmType, String url, String content,
		List<AlbumMemberListResponse> memberList) {
		this.alarmType = alarmType;
		this.url = url;
		this.content = content;
		this.memberList = memberList;
	}

	public static InviteAlarmDetailResponse of(AlarmType alarmType, Memory memory, File file) {
		return InviteAlarmDetailResponse.builder()
			.alarmType(alarmType)
			.content(memory.getContent())
			.url(file.getUrl())
			.build();
	}

	public static InviteAlarmDetailResponse of(AlarmType alarmType, List<AlbumMemberList> albumMemberLists) {
		return InviteAlarmDetailResponse.builder()
			.alarmType(alarmType)
			.memberList(albumMemberLists.stream().map(AlbumMemberListResponse::from).toList())
			.build();
	}

}
