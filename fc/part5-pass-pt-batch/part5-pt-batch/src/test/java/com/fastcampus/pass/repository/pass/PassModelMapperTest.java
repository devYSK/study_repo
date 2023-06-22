package com.fastcampus.pass.repository.pass;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PassModelMapperTest {
	@Test
	public void test_toPassEntity() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final String userId = "A1000000";

		BulkPassEntity bulkPassEntity = new BulkPassEntity();
		bulkPassEntity.setPackageSeq(1);
		bulkPassEntity.setUserGroupId("GROUP");
		bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
		bulkPassEntity.setCount(10);
		bulkPassEntity.setStartedAt(now.minusDays(60));
		bulkPassEntity.setEndedAt(now);

		// when
		final PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);

		// then
		assertEquals(1, passEntity.getPackageSeq());
		assertEquals(PassStatus.READY, passEntity.getStatus());
		assertEquals(10, passEntity.getRemainingCount());
		assertEquals(now.minusDays(60), passEntity.getStartedAt());
		assertEquals(now, passEntity.getEndedAt());

	}
}