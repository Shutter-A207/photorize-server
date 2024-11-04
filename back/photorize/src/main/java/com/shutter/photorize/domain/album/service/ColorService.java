package com.shutter.photorize.domain.album.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.album.dto.response.ColorListResponse;
import com.shutter.photorize.domain.album.entity.Color;
import com.shutter.photorize.domain.album.repository.ColorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColorService {

	private final ColorRepository colorRepository;

	public ColorListResponse getColorList() {
		List<Color> colors = colorRepository.findAll();
		return new ColorListResponse(colors);
	}
}
