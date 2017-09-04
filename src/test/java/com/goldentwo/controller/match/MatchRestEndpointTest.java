package com.goldentwo.controller.match;

import com.goldentwo.controller.MatchRestEndpoint;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.MatchPlayerSummaryDto;
import com.goldentwo.dto.MatchSummaryDto;
import com.goldentwo.dto.TurnDto;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.Turn;
import com.goldentwo.model.TurnWinType;
import com.goldentwo.service.MatchService;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class MatchRestEndpointTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchRestEndpoint sut;

    private MatchDto matchOne;
    private MatchDto matchTwo;

    private MatchSummaryDto matchSummary;

    private TurnDto turnOne;
    private TurnDto turnTwo;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        matchOne = MatchDto.builder().id(1L).build();
        matchTwo = MatchDto.builder().id(2L).build();

        Map<String, Integer> kills = new HashMap<>();
        kills.put("Taz", 2);
        kills.put("Snax", 1);

        turnOne = TurnDto.builder().no(1).kills(kills).deaths(Sets.newHashSet("Pasha", "Snax", "Neo"))
                .winner(1L).winType(TurnWinType.DEFUSE).build();

        turnTwo = TurnDto.builder().id(1L).no(1).kills(kills).deaths(Sets.newHashSet("Pasha", "Snax", "Neo"))
                .winner(1L).winType(TurnWinType.DEFUSE).build();

        Player playerOne = Player.builder().id(1L).nickname("Taz").build();
        Player playerTwo = Player.builder().id(2L).nickname("Pasha").build();

        Player playerThree = Player.builder().id(3L).nickname("Byali").build();
        Player playerFour = Player.builder().id(4L).nickname("Neo").build();
        Player playerFive = Player.builder().id(5L).nickname("Snax").build();

        Team teamOne = Team.builder()
                .id(1L)
                .name("GoldenTwo")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();

        Team teamTwo = Team.builder()
                .name("GoldenFive")
                .id(2L)
                .players(Sets.newHashSet(playerThree, playerFour, playerFive))
                .build();

        MatchPlayerSummaryDto playerOneStats = MatchPlayerSummaryDto.builder()
                .playerNickname("Taz").kills(2).deaths(0).build();

        MatchPlayerSummaryDto playerTwoStats = MatchPlayerSummaryDto.builder()
                .playerNickname("Pasha").kills(0).deaths(1).build();

        MatchPlayerSummaryDto playerThreeStats = MatchPlayerSummaryDto.builder()
                .playerNickname("Snax").kills(1).deaths(1).build();

        MatchPlayerSummaryDto playerFourStats = MatchPlayerSummaryDto.builder()
                .playerNickname("Neo").kills(0).deaths(1).build();

        MatchPlayerSummaryDto playerFveStats = MatchPlayerSummaryDto.builder()
                .playerNickname("Byali").kills(0).deaths(0).build();

        matchSummary = MatchSummaryDto.builder().matchId(4L).ended(true)
                .scoreTeamOne(16).scoreTeamTwo(4).teamOne(teamOne.asDto()).teamTwo(teamTwo.asDto())
                .teamOneStatistics(Sets.newHashSet(playerOneStats, playerTwoStats))
                .teamTwoStatistics(Sets.newHashSet(playerThreeStats, playerFourStats, playerFveStats))
                .build();
    }

    @Test
    public void findAllMatchesTest() {
        List<MatchDto> matches = Arrays.asList(matchOne, matchTwo);

        Mockito
                .when(matchService.findAllMatches())
                .thenReturn(matches);

        List<MatchDto> matchesFromSut = sut.findAllMatches();

        assertThat(matchesFromSut)
                .isNotNull()
                .isEqualTo(matches);
    }

    @Test
    public void findMatchByIdTest() {
        Long matchId = matchOne.getId();

        Mockito
                .when(matchService.findMatchById(matchId))
                .thenReturn(matchOne);

        MatchDto matchFromSut = sut.findMatchById(matchId);

        assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    @Test
    public void getMatchSummaryTest() {
        Long matchId = 4L;

        Mockito
                .when(matchService.getMatchSummary(matchId))
                .thenReturn(matchSummary);

        MatchSummaryDto summaryFromSut = sut.getMatchSummary(matchId);

        assertThat(summaryFromSut)
                .isNotNull()
                .isEqualTo(matchSummary);
    }

    @Test
    public void saveMatchTest() {
        Mockito
                .when(matchService.createMatch(matchOne))
                .thenReturn(matchOne);

        MatchDto savedMatchFromSut = sut.saveMatch(matchOne);

        assertThat(savedMatchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    @Test
    public void addTurnTest() {
        Long matchId = 1L;

        Mockito
                .when(matchService.addTurn(matchId, turnOne))
                .thenReturn(turnTwo);

        TurnDto turnFromSut = sut.addTurn(matchId, turnOne);

        assertThat(turnFromSut)
                .isNotNull()
                .isEqualTo(turnTwo);
    }

    @Test
    public void deleteMatchTest() {
        Long id = 1L;
        ResponseEntity expectedResponse = ResponseEntity.ok().build();

        Mockito
                .when(matchService.deleteMatch(id))
                .thenReturn(expectedResponse);

        ResponseEntity responseFromSut = sut.deleteMatch(id);

        assertThat(responseFromSut)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }


}