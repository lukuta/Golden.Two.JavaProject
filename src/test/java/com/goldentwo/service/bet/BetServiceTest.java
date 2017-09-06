package com.goldentwo.service.bet;

import com.goldentwo.dto.BetDto;
import com.goldentwo.model.Match;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import com.goldentwo.model.bet.BetType;
import com.goldentwo.repository.BettingRepository;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.service.impl.BetServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

public class BetServiceTest {

    public static final String TEST_NAME = "test";
    private Principal principal;

    @Mock
    private BettingRepository bettingRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private BetServiceImpl sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        principal = () -> TEST_NAME;
    }

    @Test
    public void ensureThatAllUserBetsAreReturned() {
        Mockito
                .when(bettingRepository.findAllByTyper(TEST_NAME))
                .thenReturn(Arrays.asList(
                        Bet.builder().typer("test").id(1L).build(),
                        Bet.builder().typer("test").id(2L).build())
                );

        List<Bet> sutOwnBets = sut.getOwnBets(principal);

        assertThat(sutOwnBets)
                .hasSize(2);
    }

    @Test
    public void createBetTest() {
        Match match = Match.builder().id(1L).build();

        Bet bet = Bet.builder()
                .betStatus(BetStatus.OPEN)
                .typer(TEST_NAME)
                .match(match)
                .betType(BetType.ONE)
                .build();
        Bet betWithId = Bet.builder()
                .id(1L)
                .betStatus(BetStatus.OPEN)
                .typer(TEST_NAME)
                .betStatus(BetStatus.OPEN)
                .build();


        Mockito
                .when(matchRepository.findOne((Long) any()))
                .thenReturn(match);
        Mockito
                .when(bettingRepository.saveAndFlush(bet))
                .thenReturn(betWithId);

        Bet sutBet = sut.createBet(principal, BetDto.builder().matchId(1L).betType(BetType.ONE).build());

        assertThat(sutBet)
                .isEqualTo(betWithId);
    }

}