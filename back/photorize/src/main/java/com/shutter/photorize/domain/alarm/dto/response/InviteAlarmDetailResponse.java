package com.shutter.photorize.domain.alarm.dto.response;

import com.shutter.photorize.domain.album.dto.response.AlbumMemberListResponse;
import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import com.shutter.photorize.domain.file.entity.File;
import com.shutter.photorize.domain.memory.entity.Memory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InviteAlarmDetailResponse {

    private String url;
    private String content;
    private List<AlbumMemberListResponse> memberList;

    @Builder
    public InviteAlarmDetailResponse(String url, String content, List<AlbumMemberListResponse> memberList) {
        this.url = url;
        this.content = content;
        this.memberList = memberList;
    }

    public static InviteAlarmDetailResponse of(Memory memory, File file) {
        return InviteAlarmDetailResponse.builder()
                .content(memory.getContent())
                .url(file.getUrl())
                .build();
    }

    public static InviteAlarmDetailResponse of(List<AlbumMemberList> albumMemberLists) {
        return InviteAlarmDetailResponse.builder()
                .memberList(albumMemberLists.stream().map(AlbumMemberListResponse::from).toList())
                .build();
    }

}
