package com.goldentwo.controller;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MatchRestEndpointFullIntegrationTest {

    @Value("${test.db.init.matches.size}")
    private int matchesSize;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void ensureThatAllMatchesAreReturnedFromEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/matches/", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        JsonPath jsonPath = new JsonPath(response.getBody());
        List<Long> ids = jsonPath.get("id");

        assertThat(ids)
                .isNotNull()
                .hasSize(matchesSize);
    }
}
