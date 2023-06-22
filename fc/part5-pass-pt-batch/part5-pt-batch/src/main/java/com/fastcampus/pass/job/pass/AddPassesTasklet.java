package com.fastcampus.pass.job.pass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.fastcampus.pass.repository.pass.BulkPassEntity;
import com.fastcampus.pass.repository.pass.BulkPassRepository;
import com.fastcampus.pass.repository.pass.BulkPassStatus;
import com.fastcampus.pass.repository.pass.PassEntity;
import com.fastcampus.pass.repository.pass.PassModelMapper;
import com.fastcampus.pass.repository.pass.PassRepository;
import com.fastcampus.pass.repository.user.UserGroupMappingEntity;
import com.fastcampus.pass.repository.user.UserGroupMappingRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component()
@AllArgsConstructor
public class AddPassesTasklet implements Tasklet {

	private final PassRepository passRepository;
	private final BulkPassRepository bulkPassRepository;
	private final UserGroupMappingRepository userGroupMappingRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
		// 이용권 시작 일시 1일 전 user group 내 각 사용자에게 이용권을 추가해줍니다.
		final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);

		final List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(
			BulkPassStatus.READY, startedAt);

		int count = 0;

		for (BulkPassEntity bulkPassEntity : bulkPassEntities) {

			final List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
				.stream().map(UserGroupMappingEntity::getUserId).toList();

			count += addPasses(bulkPassEntity, userIds);

		}

		log.info("AddPassesTasklet - execute: 이용권 {}건 추가 완료, startedAt={}", count, startedAt);
		return RepeatStatus.FINISHED;
	}

	// bulkPass의 정보로 pass 데이터를 생성합니다.
	private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds) {
		List<PassEntity> passEntities = new ArrayList<>();

		for (String userId : userIds) {
			PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);
			passEntities.add(passEntity);

		}
		return passRepository.saveAll(passEntities).size();
	}

}
