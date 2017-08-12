package com.goldentwo.controller.tournament;

import com.goldentwo.controller.TournamentRestEndpoint;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.service.TournamentService;
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
public class TournamentRestEndpointTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentRestEndpoint sut;

    private TeamDto teamOne;
    private TeamDto teamTwo;
    private TournamentDto tournamentOne;
    private TournamentDto tournamentTwo;

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

        tournamentOne = TournamentDto.builder()
                .id(1L)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamTwo))
                .build();

        tournamentOne = TournamentDto.builder()
                .id(2L)
                .name("PGL")
                .teams(Sets.newHashSet(teamTwo, teamOne))
                .build();
    }

    @Test
    public void findAllTournamentsTest() {
        List<TournamentDto> tournaments = Arrays.asList(tournamentOne, tournamentTwo);

        Mockito
                .when(tournamentService.findAllTournaments())
                .thenReturn(tournaments);

        List<TournamentDto> tournamentsFromSut = sut.findAllTournaments();

        assertThat(tournamentsFromSut)
                .isNotNull()
                .isEqualTo(tournaments);
    }

    @Test
    public void findTournamentByIdWhenExist() {
        Long tournamentId = 1L;

        Mockito
                .when(tournamentService.findTournamentById(tournamentId))
                .thenReturn(tournamentOne);

        TournamentDto tournamentFromSut = sut.findTournamentById(tournamentId);

        assertThat(tournamentFromSut)
                .isNotNull()
                .isEqualTo(tournamentOne);
    }

}
