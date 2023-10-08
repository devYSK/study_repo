package com.ys.test;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	@Value("${aws.endpoint}")
	String awsEndpoint;

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		return AwsCredentialsProviderChain.builder()
										  .reuseLastProviderEnabled(true)
										  .credentialsProviders(List.of(
											  DefaultCredentialsProvider.create(),
											  StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar"))
										  ))
										  .build();
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
					   .credentialsProvider(awsCredentialsProvider())
					   .region(Region.AP_NORTHEAST_2)
					   .endpointOverride(URI.create(awsEndpoint))
					   .build();
	}
}