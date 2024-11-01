package com.shutter.photorize.domain.file.entity;

import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.entity.UpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends UpdatableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memory_id", nullable = false)
	private Memory memory;

	@Column(name = "type", columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private FileType fileType;

	@Column(nullable = false, unique = true)
	private String url;

	@Builder
	private File(Memory memory, FileType fileType, String url) {
		this.memory = memory;
		this.fileType = fileType;
		this.url = url;
	}

	public static File of(Memory memory, FileType fileType, String url) {
		return File.builder()
			.memory(memory)
			.fileType(fileType)
			.url(url)
			.build();
	}
}
