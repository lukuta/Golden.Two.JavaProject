package com.goldentwo.controller.team;


import com.goldentwo.controller.TeamRestEndpoint;
import com.goldentwo.dto.TeamDto;
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

    private TeamDto teamOne;
    private TeamDto teamTwo;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        teamOne = TeamDto.builder()
                .name("GoldenTwo")
                .playerNicknames(Sets.newHashSet("qtek", "klimeck"))
                .build();
        teamTwo = TeamDto.builder()
                .name("GoldenFive")
                .playerNicknames(Sets.newHashSet("Taz", "Pasha", "Byali", "Neo", "Snax"))
                .build();
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
}
