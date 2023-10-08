package com.ys.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

class S3ServiceTest extends IntegrationTest {
	@Autowired
	private S3Service s3Service;

	@Test
	void s3PutAndGetTest() throws Exception {
		// given
		var key = "sampelObject.txt";
		var sampleFile = new ClassPathResource("static/sample.txt").getFile();

		// when
		s3Service.putFile("test-bucket", key, sampleFile);

		// then
		var resultFile = s3Service.getFile("test-bucket", key);

		var sampleFileLines = Files.readAllLines(Paths.get(sampleFile.getPath()));
		var responseFileLines = Files.readAllLines(Paths.get(resultFile.getPath()));


		System.out.println("============================== here");
		System.out.println(sampleFile.length());
		System.out.println(resultFile.length());
		System.out.println(Arrays.toString(sampleFileLines.toArray()));
		System.out.println(Arrays.toString(responseFileLines.toArray()));
		System.out.println("============================== here");

		Assertions.assertIterableEquals(sampleFileLines, responseFileLines);
	}
}