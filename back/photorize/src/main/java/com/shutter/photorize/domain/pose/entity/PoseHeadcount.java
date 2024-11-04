package com.shutter.photorize.domain.pose.entity;

import com.shutter.photorize.global.error.ErrorType;
import com.shutter.photorize.global.exception.PhotorizeException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PoseHeadcount {

	ONE(1),
	TWO(2),
	THREE_OR_FOUR(3),
	FIVE_OR_MORE(5);

	private final int value;

	public static PoseHeadcount convertToEnum(int headcount) {
		return switch (headcount) {
			case 1 -> ONE;
			case 2 -> TWO;
			case 3, 4 -> THREE_OR_FOUR;
			case 5, 6, 7, 8, 9 -> FIVE_OR_MORE;
			default -> throw new PhotorizeException(ErrorType.NO_POSE_FOUND);
		};
	}
}
