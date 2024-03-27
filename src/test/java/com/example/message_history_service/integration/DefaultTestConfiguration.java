package com.example.message_history_service.integration;


import com.example.message_history_service.integration.request_util.RequestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class DefaultTestConfiguration {
    @Autowired
    protected WebTestClient webTestClient;


    @LocalServerPort
    protected int serverPort;


    protected RequestUtil createRequestUtil() {
        return new RequestUtil(serverPort);
    }

    @BeforeEach
    public void clearAllDatabaseInDatabaseBeforeRunTest() {

    }


}
