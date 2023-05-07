package com.ys.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.ys.cafekiosk.spring.client.mail.MailSendClient;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

	@MockBean
	protected MailSendClient mailSendClient;

}