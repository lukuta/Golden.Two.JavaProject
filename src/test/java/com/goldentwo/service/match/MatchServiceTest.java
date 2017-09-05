package com.goldentwo.service.match;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.MatchPlayerSummaryDto;
import com.goldentwo.dto.MatchSummaryDto;
import com.goldentwo.dto.TurnDto;
import com.goldentwo.exception.BadRequestException;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.*;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.repository.TurnRepository;
import com.goldentwo.service.impl.MatchServiceImpl;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class MatchServiceTest {
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TurnRepository turnRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private MatchServiceImpl sut;

    private Player playerOne;
    private Player playerTwo;

    private Player updatedPlayerOne;
    private Player updatedPlayerTwo;

    private Team teamOne;

    private Team updatedTeamOne;

    private Turn turnOne;
    private Turn turnTwo;
    private TurnDto turnOneDto;
    private TurnDto turnTwoDto;


    private Match matchOne;
    private Match matchTwo;
    private Match matchThree;
    private Match matchFour;
    private Match matchFive;
    private Match matchSix;

    private MatchDto matchOneDto;
    private MatchDto matchTwoDto;
    private MatchDto matchThreeDto;

    private MatchDto endedMatch;

    private MatchSummaryDto matchSummary;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        playerOne = Player.builder().id(1L).nickname("Taz").rankPoints(300).build();
        playerTwo = Player.builder().id(2L).nickname("Pasha").rankPoints(200).build();

        updatedPlayerOne = Player.builder().id(1L).nickname("Taz").rankPoints(350).build();
        updatedPlayerTwo = Player.builder().id(2L).nickname("Pasha").rankPoints(250).build();

        Player playerThree = Player.builder().id(3L).nickname("Byali").build();
        Player playerFour = Player.builder().id(4L).nickname("Neo").build();
        Player playerFive = Player.builder().id(5L).nickname("Snax").build();

        teamOne = Team.builder()
                .id(1L)
                .name("GoldenTwo")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .rankPoints(50)
                .build();

        updatedTeamOne = Team.builder()
                .id(1L)
                .name("GoldenTwo")
                .players(Sets.newHashSet(updatedPlayerOne, updatedPlayerTwo))
                .rankPoints(100)
                .build();

        Team teamTwo = Team.builder()
                .name("GoldenFive")
                .id(2L)
                .players(Sets.newHashSet(playerThree, playerFour, playerFive))
                .build();

        Map<String, Integer> kills = new HashMap<>();
        kills.put("Taz", 2);
        kills.put("Snax", 1);

        turnOne = Turn.builder().no(1).kills(kills).deaths(Sets.newHashSet("Pasha", "Snax", "Neo"))
                .winner(1L).winType(TurnWinType.DEFUSE).build();

        turnOneDto = turnOne.asDto();

        turnTwo = Turn.builder().id(1L).no(1).kills(kills).deaths(Sets.newHashSet("Pasha", "Snax", "Neo"))
                .winner(1L).winType(TurnWinType.DEFUSE).build();

        turnTwoDto = turnTwo.asDto();

        matchOne = Match.builder().id(1L).teamOne(teamOne).teamTwo(teamTwo)
                .turns(new HashSet<>())
                .scoreTeamOne(1).scoreTeamTwo(4).ended(false).build();

        matchOneDto = matchOne.asDto();

        matchTwo = Match.builder().id(2L).teamTwo(teamOne).teamOne(teamTwo)
                .turns(new HashSet<>())
                .scoreTeamTwo(3).scoreTeamOne(16).ended(true).build();

        matchTwoDto = matchTwo.asDto();

        matchThree = Match.builder().teamOne(teamOne).teamTwo(teamTwo)
                .turns(new HashSet<>())
                .scoreTeamOne(1).scoreTeamTwo(4).ended(false).build();

        matchThreeDto = matchThree.asDto();

        matchFour = Match.builder().id(1L).teamOne(teamOne).teamTwo(teamTwo)
                .turns(Sets.newHashSet(turnTwo))
                .scoreTeamOne(2).scoreTeamTwo(4).ended(false).build();

        matchFive = Match.builder().id(4L).teamOne(teamOne).teamTwo(teamTwo)
                .turns(Sets.newHashSet())
                .scoreTeamOne(15).scoreTeamTwo(4).ended(false).build();

        matchSix = Match.builder().id(4L).teamOne(updatedTeamOne).teamTwo(teamTwo)
                .turns(Sets.newHashSet(turnTwo))
                .scoreTeamOne(16).scoreTeamTwo(4).ended(true).build();

        endedMatch = MatchDto.builder().id(3L).teamOne(teamOne.asDto()).teamTwo(teamTwo.asDto())
                .turns(new HashSet<>()).scoreTeamOne(1).scoreTeamTwo(16)
                .ended(true).build();

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
                .scoreTeamOne(16).scoreTeamTwo(4).teamOne(updatedTeamOne.asDto()).teamTwo(teamTwo.asDto())
                .teamOneStatistics(Sets.newHashSet(playerOneStats, playerTwoStats))
                .teamTwoStatistics(Sets.newHashSet(playerThreeStats, playerFourStats, playerFveStats))
                .build();
    }

    @Test
    public void findAllMatchesTest() {
        Mockito
                .when(matchRepository.findAll())
                .thenReturn(Arrays.asList(matchOne, matchTwo));

        List<MatchDto> matchesFromSut = sut.findAllMatches();

        assertThat(matchesFromSut).isNotNull()
                .isEqualTo(Arrays.asList(matchOneDto, matchTwoDto));
    }

    @Test
    public void findMatchByIdWhenExistTest() {
        Long matchId = 1L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(matchOne);

        MatchDto matchFromSut = sut.findMatchById(matchId);

        assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOneDto);
    }

    @Test(expected = NotFoundException.class)
    public void findMatchByIdWhenNotExistTest() {
        Long matchId = 3L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(null);

        sut.findMatchById(matchId);
    }

    @Test
    public void createMatchTest() {
        Mockito
                .when(matchRepository.save(matchThree))
                .thenReturn(matchOne);

        MatchDto matchFromSut = sut.createMatch(matchThreeDto);

        assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOneDto);
    }

    @Test
    public void updateMatchTest() {
        Mockito
                .when(matchRepository.save(matchOne))
                .thenReturn(matchOne);

        MatchDto matchFromSut = sut.updateMatch(matchOneDto);

        assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOneDto);
    }

    @Test(expected = BadRequestException.class)
    public void updateMatchWithMissingIdTest() {
        sut.updateMatch(matchThreeDto);
    }

    @Test(expected = NotFoundException.class)
    public void addTurnWhenMatchNotFound() {
        Long matchId = 1L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(null);

        sut.addTurn(matchId, turnOneDto);
    }

    @Test(expected = BadRequestException.class)
    public void addTurnWhenMatchIsEnded() {
        Long matchId = 3L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(endedMatch.asEntity());

        sut.addTurn(matchId, turnOneDto);
    }

    @Test
    public void addTurnTest() {
        Long matchId = 1L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(matchOne);

        Mockito
                .when(turnRepository.save(turnOne))
                .thenReturn(turnTwo);

        Mockito
                .when(matchRepository.save(matchFour))
                .thenReturn(matchFour);

        TurnDto turnFromSut = sut.addTurn(matchId, turnOneDto);

        assertThat(turnFromSut)
                .isNotNull()
                .isEqualTo(turnTwoDto);
    }

    @Test
    public void endMatchAfterAddLastTurnTest() {
        Long matchId = 4L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(matchFive);

        Mockito
                .when(turnRepository.save(turnOne))
                .thenReturn(turnTwo);

        Mockito
                .when(matchRepository.save(matchSix))
                .thenReturn(matchSix);

        TurnDto turnFromSut = sut.addTurn(matchId, turnOneDto);

        assertThat(turnFromSut)
                .isNotNull()
                .isEqualTo(turnTwoDto);
    }

    @Test
    public void getMatchSummaryTest() {
        Long matchId = 4L;

        Mockito
                .when(matchRepository.findOne(matchId))
                .thenReturn(matchSix);

        MatchSummaryDto summaryFromSut = sut.getMatchSummary(matchId);

        assertThat(summaryFromSut)
                .isNotNull()
                .isEqualToComparingOnlyGivenFields(matchSummary, "matchId", "teamOne", "teamTwo", "ended", "scoreTeamOne", "scoreTeamTwo");

        assertThat(summaryFromSut.getTeamOneStatistics())
                .isNotEmpty()
                .containsOnlyElementsOf(matchSummary.getTeamOneStatistics());

        assertThat(summaryFromSut.getTeamTwoStatistics())
                .isNotEmpty()
                .containsOnlyElementsOf(matchSummary.getTeamTwoStatistics());
    }

}
