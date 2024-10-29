package com.shutter.photorize.domain.album.dto.request;

import java.util.List;

import lombok.Getter;

@Getter
public class AlbumCreateRequest {
	private String name;
	private List<Long> members;
}