package com.fastcampus.pass.job.notification;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import com.fastcampus.pass.adapter.KakaoTalkMessageAdapter;
import com.fastcampus.pass.config.JpaConfig;
import com.fastcampus.pass.config.KakaoTalkMessageConfig;
import com.fastcampus.pass.config.P6spyConfiguration;
import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingRepository;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassRepository;
import com.fastcampus.pass.repository.pass.PassStatus;
import com.fastcampus.pass.repository.user.UserEntity;
import com.fastcampus.pass.repository.user.UserRepository;
import com.fastcampus.pass.repository.user.UserStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
	SendNotificationBeforeClassJobConfig.class,
	TestBatchConfig.class,
	SendNotificationItemWriter.class,
	KakaoTalkMessageConfig.class,
	KakaoTalkMessageAdapter.class,
	P6spyConfiguration.class,
	// JpaConfig.class
})
class SendNotificationBeforeClassJobConfigTest {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private PassRepository passRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void test_addNotificationStep() throws Exception {
		// given
		addBookingEntity();

		// when
		log.info("step start");
		JobExecution addNotificationStep = jobLauncherTestUtils.launchStep("addNotificationStep");
		JobExecution sendNotificationStep = jobLauncherTestUtils.launchStep("sendNotificationStep");
		log.info("step stop");
		// then
		assertThat(addNotificationStep.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
	}

	private void addBookingEntity() {
		final LocalDateTime now = LocalDateTime.now();
		final String userId = "A100" + RandomStringUtils.randomNumeric(4);

		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userId);
		userEntity.setUserName("김영희");
		userEntity.setStatus(UserStatus.ACTIVE);
		userEntity.setPhone("01033334444");
		userEntity.setMeta("abcd1234");
		userRepository.save(userEntity);

		PassEntity passEntity = new PassEntity();
		passEntity.setPackageSeq(1);
		passEntity.setUserId(userId);
		passEntity.setStatus(PassStatus.IN_PROGRESS);
		passEntity.setRemainingCount(10);
		passEntity.setStartedAt(now.minusDays(60));
		passEntity.setEndedAt(now.minusDays(1));
		passRepository.save(passEntity);

		BookingEntity bookingEntity = new BookingEntity();
		bookingEntity.setPassSeq(passEntity.getPassSeq());
		bookingEntity.setUserId(userId);
		bookingEntity.setStatus(BookingStatus.READY);
		bookingEntity.setStartedAt(now.plusMinutes(10));
		bookingEntity.setEndedAt(bookingEntity.getStartedAt().plusMinutes(50));
		bookingRepository.save(bookingEntity);

	}

}