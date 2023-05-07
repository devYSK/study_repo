package com.ys.cafekiosk.spring.client.mail;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailSendClient {

	public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
		log.info("메일 전송");
		throw new IllegalArgumentException("메일 전송");
	}

}