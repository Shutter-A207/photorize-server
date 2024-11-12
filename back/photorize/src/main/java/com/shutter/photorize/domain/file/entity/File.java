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

	@Column(columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private FileType type;

	@Column(nullable = false)
	private String url;

	@Builder
	private File(Memory memory, FileType type, String url) {
		this.memory = memory;
		this.type = type;
		this.url = url;
	}

	public static File of(Memory memory, FileType type, String url) {
		return File.builder()
			.memory(memory)
			.type(type)
			.url(url)
			.build();
	}
}
