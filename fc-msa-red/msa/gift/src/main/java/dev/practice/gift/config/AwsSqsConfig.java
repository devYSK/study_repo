package dev.practice.gift.config;

import static software.amazon.awssdk.regions.Region.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsSqsConfig {
	@Value("${cloud.aws.access-key}")
	private String awsAccessKey;

	@Value("${cloud.aws.secret-key}")
	private String awsSecretKey;

	@Bean
	public SqsAsyncClient sqsAsyncClient(
	) {
		var awsCredentialsProvider = StaticCredentialsProvider.create(
			AwsBasicCredentials.create(awsAccessKey, awsSecretKey));

		return SqsAsyncClient.builder()
			.region(AP_NORTHEAST_2)
			.credentialsProvider(awsCredentialsProvider)
			.build();
	}
}
