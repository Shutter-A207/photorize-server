package com.shutter.photorize.domain.album.dto.response;

import com.shutter.photorize.domain.album.entity.AlbumMemberList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlbumMemberListResponse {
    private String nickname;
    private String img;
    private boolean status;

    @Builder
    private AlbumMemberListResponse(String nickname, String img, boolean status) {
        this.nickname = nickname;
        this.img = img;
        this.status = status;
    }

    public static AlbumMemberListResponse from(AlbumMemberList albumMemberList) {
        return AlbumMemberListResponse.builder()
                .nickname(albumMemberList.getMember().getNickname())
                .img(albumMemberList.getMember().getImg())
                .status(albumMemberList.isStatus())
                .build();
    }
}
