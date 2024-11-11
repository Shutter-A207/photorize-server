package com.shutter.photorize.domain.alarm.entity;

import org.hibernate.annotations.SQLRestriction;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.memory.entity.Memory;
import com.shutter.photorize.global.entity.SoftDeletableEntity;

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
@SQLRestriction("deleted_at is NULL")
public class InviteAlarm extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id")
	private Album album;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memory_id")
	private Memory memory;

	@Column(nullable = false)
	private boolean checked;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AlarmType type;

	@Builder
	private InviteAlarm(Long id, Member member, Album album, Memory memory, boolean checked, AlarmType type) {
		this.id = id;
		this.member = member;
		this.memory = memory;
		this.checked = checked;
		this.type = type;
	}

	public static InviteAlarm of(Member member, Memory memory) {
		return InviteAlarm.builder()
			.member(member)
			.memory(memory)
			.type(AlarmType.PRIVATE)
			.build();
	}

	public static InviteAlarm of(Member member, Album album) {
		return InviteAlarm.builder()
			.member(member)
			.album(album)
			.type(AlarmType.PUBLIC)
			.build();
	}
}
