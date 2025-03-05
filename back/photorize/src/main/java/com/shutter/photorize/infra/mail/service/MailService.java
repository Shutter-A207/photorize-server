package com.shutter.photorize.infra.mail.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutter.photorize.global.constant.StringFormat;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;

	@Async("mailSendExecutor")
	public void sendEmail(String to, String subject, String content, boolean isHtml) {
		log.info("Executing sendEmail in thread: {}", Thread.currentThread().getName());

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content, isHtml);
			mimeMessageHelper.setFrom(new InternetAddress(StringFormat.EMAIL_ADDRESS, StringFormat.EMAIL_PERSONAL));

			javaMailSender.send(mimeMessage);

			log.info("send mail success");
		} catch (Exception e) {
			log.error("Failed to send Email", e);
			throw new RuntimeException(e);
		}
	}
}
