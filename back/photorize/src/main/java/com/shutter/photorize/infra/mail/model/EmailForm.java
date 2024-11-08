package com.shutter.photorize.infra.mail.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode
@Getter
@Setter
public class EmailForm {
	private String to;
	private String subject;
	private String content;
	private boolean isHtml;

	private EmailForm(String to, String subject, String content, boolean isHtml) {
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.isHtml = isHtml;
	}

	public static EmailForm of(String to, String subject, String content, boolean isHtml) {
		return new EmailForm(to, subject, content, isHtml);
	}
}
