package com.goldentwo.controller.team;


import com.goldentwo.controller.TeamRestEndpoint;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TeamStatisticsDto;
import com.goldentwo.model.Player;
import com.goldentwo.service.TeamService;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class TeamRestEndpointTest {
    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamRestEndpoint sut;

    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private PlayerDto playerThree;
    private PlayerDto playerFour;
    private TeamDto teamOne;
    private TeamDto teamTwo;

    private TeamStatisticsDto teamStats;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        playerOne = PlayerDto.builder()
                .id(1L)
                .nickname("Taz")
                .name("Wiktor")
                .surname("Wojtas")
                .build();

        playerTwo = PlayerDto.builder()
                .id(2L)
                .nickname("Neo")
                .name("Filip")
                .surname("Kubski")
                .build();

        playerThree = PlayerDto.builder()
                .id(3L)
                .nickname("olofmeister")
                .name("Olof")
                .surname("Kyaber")
                .build();

        playerFour = PlayerDto.builder()
                .id(4L)
                .nickname("JW")
                .name("Jaspher")
                .surname("Wild")
                .build();

        teamOne = TeamDto.builder()
                .name("GoldenTwo")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();
        teamTwo = TeamDto.builder()
                .name("GoldenFive")
                .players(Sets.newHashSet(playerThree, playerFour))
                .build();

        teamStats = TeamStatisticsDto.builder().wins(2).defeats(1).wd(2).teamId(teamOne.getId()).build();
    }

    @Test
    public void getAllTeamsTest() {
        List<TeamDto> teams = Arrays.asList(teamOne, teamTwo);

        Mockito
                .when(teamService.findAllTeams())
                .thenReturn(teams);

        List<TeamDto> teamsFromSut = sut.getAllTeams();

        assertThat(teamsFromSut)
                .isNotNull()
                .isEqualTo(teams);
    }

    @Test
    public void findTeamByIdTest() {
        Long teamId = 1L;

        Mockito
                .when(teamService.findTeamById(teamId))
                .thenReturn(teamOne);

        TeamDto teamFromSut = sut.findTeamById(teamId);

        assertThat(teamFromSut)
                .isNotNull()
                .isEqualTo(teamOne);
    }

    @Test
    public void findTeamStatisticsTest() {
        Long teamId = 1L;

        Mockito
                .when(teamService.findTeamStatistics(teamId))
                .thenReturn(teamStats);

        TeamStatisticsDto statsFromSut = sut.findTeamStatistics(teamId);

        assertThat(statsFromSut)
                .isNotNull()
                .isEqualTo(teamStats);
    }
}
