package com.goldentwo.repository;

import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BetRepositoryTest {

    @Autowired
    private BettingRepository bettingRepository;

    @Value("${test.db.init.bets.size}")
    private int dbSize;

    @Value("${test.db.init.bets.typer.amount}")
    private int typerAmount;

    @Before
    public void init() {
        tearDown();
        for (int i = 0; i < dbSize; i++) {
            bettingRepository.saveAndFlush(
                    Bet.builder()
                            .betStatus(i % 2 == 0 ? BetStatus.OPEN : BetStatus.CLOSED)
                            .typer("typer" + (i % typerAmount))
                            .build()
            );
        }
    }

    @After
    public void tearDown() {
        bettingRepository.deleteAll();
    }

    @Test
    public void ensureThatRepositoryFoundAllBetsByTyper() {
        List<Bet> allByTyper = bettingRepository.findAllByTyper("typer" + (typerAmount - 1));
        assertThat(allByTyper)
                .hasSize(dbSize / typerAmount);
    }

    @Test
    public void ensureThatRepositoryFoundAllOpenBets() {
        List<Bet> allByBetStatus = bettingRepository.findAllByBetStatus(BetStatus.OPEN);
        assertThat(allByBetStatus)
                .hasSize(dbSize / 2);
    }
}

