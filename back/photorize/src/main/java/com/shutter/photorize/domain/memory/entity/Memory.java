package com.shutter.photorize.domain.memory.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.shutter.photorize.domain.album.entity.Album;
import com.shutter.photorize.domain.member.entity.Member;
import com.shutter.photorize.domain.spot.entity.Spot;
import com.shutter.photorize.global.entity.SoftDeletableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Memory extends SoftDeletableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", nullable = false)
	private Album album;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_id")
	private Spot spot;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime date;

	@Column(nullable = false)
	private String content;

	@Builder
	private Memory(Member member, Album album, Spot spot, LocalDateTime date, String content) {
		this.member = member;
		this.album = album;
		this.spot = spot;
		this.date = date;
		this.content = content;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void updateSpot(Spot spot) {
		this.spot = spot;
	}

	public void updateDate(LocalDateTime date) {
		this.date = date;
	}

	public static Memory of(Member member, Album album, Memory memory) {
		return Memory.builder()
			.member(member)
			.album(album)
			.spot(memory.getSpot())
			.date(memory.getDate())
			.content(memory.getContent())
			.build();
	}
}
