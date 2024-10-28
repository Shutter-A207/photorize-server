package com.shutter.photorize.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class UpdatableEntity extends BaseEntity {

	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt;
}