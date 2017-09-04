package com.goldentwo.service.team;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TeamStatisticsDto;
import com.goldentwo.model.Match;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.impl.TeamServiceImpl;
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
import static org.mockito.Matchers.any;

@ActiveProfiles("test")
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TeamServiceImpl sut;

    private Team teamOne;
    private Team teamTwo;
    private TeamDto teamOneDto;
    private TeamDto teamTwoDto;

    private Player playerOne;
    private Player playerTwo;

    private List<Match> matches;

    private TeamStatisticsDto teamStatistics;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        playerOne = Player.builder().id(1L).nickname("Taz").build();
        playerTwo = Player.builder().id(2L).nickname("Pasha").build();

        Player playerThree = Player.builder().id(3L).nickname("Byali").build();
        Player playerFour = Player.builder().id(4L).nickname("Neo").build();
        Player playerFive = Player.builder().id(5L).nickname("Snax").build();

        teamOne = Team.builder()
                .id(1L)
                .name("GoldenTwo")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();
        teamOneDto = teamOne.asDto();

        teamTwo = Team.builder()
                .name("GoldenFive")
                .id(2L)
                .players(Sets.newHashSet(playerOne, playerTwo, playerThree, playerFour, playerFive))
                .build();
        teamTwoDto = teamTwo.asDto();

        Match matchOne = Match.builder().id(1L).ended(true).teamOne(teamOne).teamTwo(teamTwo)
                .scoreTeamOne(16).scoreTeamTwo(13).turns(new HashSet<>()).build();

        Match matchTwo = Match.builder().id(1L).ended(true).teamOne(teamOne).teamTwo(teamTwo)
                .scoreTeamOne(13).scoreTeamTwo(16).turns(new HashSet<>()).build();

        Match matchThree = Match.builder().id(1L).ended(true).teamOne(teamOne).teamTwo(teamTwo)
                .scoreTeamOne(16).scoreTeamTwo(10).turns(new HashSet<>()).build();

        matches = Arrays.asList(matchOne, matchTwo, matchThree);

        teamStatistics = TeamStatisticsDto.builder().wins(2).defeats(1).wd(2).teamId(teamOne.getId()).build();
    }

    @Test
    public void findTeamByIdTest() {
        Long teamId = 1L;

        Mockito
                .when(teamRepository.findOne(teamId))
                .thenReturn(teamOne);

        TeamDto teamFromSut = sut.findTeamById(teamId);

        assertThat(teamFromSut)
                .isNotNull()
                .isEqualTo(teamOneDto);
    }

    @Test
    public void findTeamByNameTest() {
        String teamName = teamOne.getName();

        Optional<Team> expectedData = Optional.ofNullable(teamOne);
        Mockito
                .when(teamRepository.findByName(teamName))
                .thenReturn(expectedData);

        TeamDto teamFromSut = sut.findTeamByName(teamName);

        assertThat(teamFromSut)
                .isNotNull()
                .isEqualTo(teamOneDto);
    }

    @Test
    public void findAllTeamsTest() {
        List<Team> teams = Arrays.asList(teamOne, teamTwo);
        List<TeamDto> expectedTeams = Arrays.asList(teamOneDto, teamTwoDto);

        Mockito
                .when(teamRepository.findAll())
                .thenReturn(teams);

        List<TeamDto> teamsFromSut = sut.findAllTeams();

        assertThat(teamsFromSut)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedTeams);
    }

    @Test
    public void saveTeamTest() {
        mockTeamRepository();
        mockPlayerRepository();

        TeamDto savedTeamFromSut = sut.saveTeam(teamOneDto);

        assertThat(savedTeamFromSut)
                .isNotNull()
                .isEqualTo(teamOneDto);
    }

    private void mockPlayerRepository() {
        Mockito
                .when(playerRepository.findByNickname(playerOne.getNickname()))
                .thenReturn(Optional.ofNullable(playerOne));
        Mockito
                .when(playerRepository.findByNickname(playerTwo.getNickname()))
                .thenReturn(Optional.ofNullable(playerTwo));
    }

    private void mockTeamRepository() {
        Mockito
                .when(teamRepository.saveAndFlush(any()))
                .thenReturn(teamOne);
    }

    @Test
    public void findTeamStatisticsTest() {
        Long teamId = 1L;

        Mockito.when(teamRepository.findOne(teamId))
                .thenReturn(teamOne);

        Mockito.when(matchRepository.findMatchesByTeamOneOrTeamTwoAndEnded(teamOne, teamOne, true))
                .thenReturn(Optional.of(matches));

        TeamStatisticsDto teamStatsFromSut = sut.findTeamStatistics(teamId);

        assertThat(teamStatsFromSut)
                .isNotNull()
                .isEqualTo(teamStatistics);
    }
}
