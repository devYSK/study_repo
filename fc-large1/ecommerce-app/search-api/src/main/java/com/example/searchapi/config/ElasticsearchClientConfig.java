package com.example.searchapi.config;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchClientConfig {

    @Value("${spring.elasticsearch.rest.host}")
    String host;
    @Value("${spring.elasticsearch.rest.port}")
    int port;

    @Value("${spring.elasticsearch.fingerprint}")
    String fingerprint;

    @Value("${spring.elasticsearch.account}")
    String account;

    @Value("${spring.elasticsearch.password}")
    String password;

    @Bean
    public ElasticsearchClient elasticsearchClientWithSSL() {
        // SSL 통신
        SSLContext sslContext = TransportUtils
                .sslContextFromCaFingerprint(fingerprint);

        // 인증
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(account, password)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port)) //, "https"))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();

        // 전송 객체와 클라이언트 생성
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}