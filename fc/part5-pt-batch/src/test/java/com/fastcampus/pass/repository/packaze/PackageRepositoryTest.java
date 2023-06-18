package com.fastcampus.pass.repository.packaze;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
class PackageRepositoryTest {

	@Autowired
	PackageRepository packageRepository;

	@Test
	void test_save() {

		LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

		PackageEntity packageEntity = new PackageEntity();
		packageEntity.setPackageName("바디 챌린지 PT 12주");
		packageEntity.setPeriod(84);

		PackageEntity packageEntity0 = new PackageEntity();
		packageEntity0.setPackageName("학생 전용 3개월");
		packageEntity0.setPeriod(90);
		packageRepository.save(packageEntity0);

		PackageEntity packageEntity1 = new PackageEntity();
		packageEntity1.setPackageName("$У21867|9/");
		packageEntity1.setPeriod(180);
		packageRepository.save(packageEntity1);

		//when
		final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime,
			PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

		System.out.println(packageEntities.size());

	}


}