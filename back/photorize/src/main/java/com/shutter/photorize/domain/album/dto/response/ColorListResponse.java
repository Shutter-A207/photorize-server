package com.shutter.photorize.domain.album.dto.response;

import java.util.List;

import com.shutter.photorize.domain.album.entity.Color;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ColorListResponse {
	private List<Color> colors;
}
