package com.shutter.photorize.domain.pose.dto.response;

import com.shutter.photorize.domain.pose.entity.PoseHeadcount;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PoseResponse {
	private Long poseId;
	private String headcount;
	private String img;
	private long likeCount;
	private boolean isLiked;

	private PoseResponse(Long poseId, PoseHeadcount headcount, String img, long likeCount, boolean isLiked) {
		this.poseId = poseId;
		this.headcount = headcount.name();
		this.img = img;
		this.likeCount = likeCount;
		this.isLiked = isLiked;
	}

	public static PoseResponse of(Long poseId, PoseHeadcount headcount, String img, long likeCount, boolean isLiked) {
		return new PoseResponse(poseId, headcount, img, likeCount, isLiked);
	}
}
