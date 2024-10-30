package com.shutter.photorize.domain.pose.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PoseResponse {
	private Long poseId;
	private String headcount;
	private String img;
	private int likeCount;
	private boolean isLiked;

}

