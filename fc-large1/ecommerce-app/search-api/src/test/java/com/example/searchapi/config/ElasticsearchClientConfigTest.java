package com.example.searchapi.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@SpringBootTest
class ElasticsearchClientConfigTest {

    // @MockBean
    // ProductRepository productRepository;

    @Autowired
    ElasticsearchClientConfig elasticsearchClientConfig;

    @Autowired
    private ElasticsearchClient esClient;


    @Test
    void clientConfiguration() throws IOException {


        esClient.count();

        // assertTrue(elasticsearchClientConfig.connetionUrl.contains("localhost"));
    }
}