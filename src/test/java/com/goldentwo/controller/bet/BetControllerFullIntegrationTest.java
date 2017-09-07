package com.goldentwo.controller.bet;

import com.goldentwo.model.Match;
import com.goldentwo.repository.MatchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BetControllerFullIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MatchRepository matchRepository;

    @Before
    public void init() {
        for (long i = 1; i < 9; i++) {
            matchRepository.saveAndFlush(
                    Match.builder()
                            .id(i)
                            .ended(false)
                            .build()
            );
        }
    }

    @Test
    public void test() {

    }
}
