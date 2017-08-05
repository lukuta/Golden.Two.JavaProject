package com.goldentwo.controller.match;

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

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchRestEndpointWebIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testListAll() {
        ResponseEntity<String> response = restTemplate.getForEntity("/matches/", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}
