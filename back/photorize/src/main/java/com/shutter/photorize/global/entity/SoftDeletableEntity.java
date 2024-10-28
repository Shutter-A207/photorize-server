package com.shutter.photorize.global.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
@SQLRestriction("deleted_at is NULL")
public abstract class SoftDeletableEntity extends UpdatableEntity {

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime deletedAt;

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void restore() {
		this.deletedAt = null;
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}
}