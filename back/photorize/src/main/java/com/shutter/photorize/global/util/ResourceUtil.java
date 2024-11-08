package com.shutter.photorize.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceUtil {

	private final ResourceLoader resourceLoader;

	public String getHtml(String classpath) {
		Resource resource = resourceLoader.getResource(classpath);
		Path path = null;

		try {
			path = Paths.get(resource.getURI());
			String htmlContent = new String(Files.readAllBytes(path), "UTF-8");
			return htmlContent;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
