package com.goldentwo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MatchRestEndpointWebIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testListAll() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/match/", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}
