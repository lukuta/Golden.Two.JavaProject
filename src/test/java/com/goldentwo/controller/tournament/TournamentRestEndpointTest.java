package com.goldentwo.controller.tournament;

import com.goldentwo.controller.TournamentRestEndpoint;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.exception.TournamentException;
import com.goldentwo.service.TournamentService;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.goldentwo.service.MatchesGeneratorService.MatchGeneratorType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class TournamentRestEndpointTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentRestEndpoint sut;

    private TournamentDto tournamentOne;
    private TournamentDto tournamentTwo;
    private TournamentDto tournamentWithoutId;

    private MockMvc mockMvc;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        PlayerDto playerOne = PlayerDto.builder()
                .id(1L)
                .nickname("Taz")
                .name("Wiktor")
                .surname("Wojtas")
                .build();

        PlayerDto playerTwo = PlayerDto.builder()
                .id(2L)
                .nickname("Neo")
                .name("Filip")
                .surname("Kubski")
                .build();

        PlayerDto playerThree = PlayerDto.builder()
                .id(3L)
                .nickname("olofmeister")
                .name("Olof")
                .surname("Kyaber")
                .build();

        PlayerDto playerFour = PlayerDto.builder()
                .id(4L)
                .nickname("JW")
                .name("Jaspher")
                .surname("Wild")
                .build();

        TeamDto teamOne = TeamDto.builder()
                .name("GoldenTwo")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();
        TeamDto teamTwo = TeamDto.builder()
                .name("GoldenFive")
                .players(Sets.newHashSet(playerThree, playerFour))
                .build();

        tournamentOne = TournamentDto.builder()
                .id(1L)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamTwo))
                .build();

        tournamentTwo = TournamentDto.builder()
                .id(2L)
                .name("PGL")
                .teams(Sets.newHashSet(teamTwo, teamOne))
                .build();

        tournamentWithoutId = TournamentDto.builder()
                .id(null)
                .name("PGL")
                .teams(Sets.newHashSet(teamTwo, teamOne))
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(new TournamentException(), tournamentService).build();
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

    @Test
    public void findTournamentByIdWhenNotExist() throws Exception {
        Long tournamentId = 2L;

        Mockito
                .when(tournamentService.findTournamentById(tournamentId))
                .thenThrow(new NotFoundException("Tournament " + tournamentId + "doesn't exist"));

        mockMvc.perform(get("/tournaments/{id}", tournamentId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findTournamentByName() {
        String tournamentName = "ELeague";

        Mockito
                .when(tournamentService.findTournamentByName(tournamentName))
                .thenReturn(tournamentOne);

        TournamentDto tournamentDto = sut.findTournamentByName(tournamentName);

        assertThat(tournamentDto)
                .isNotNull()
                .isEqualTo(tournamentOne);
    }

    @Test
    public void simpleSaveTournamentTest() {
        Mockito
                .when(tournamentService.saveTournament(tournamentWithoutId, MatchGeneratorType.RANDOM))
                .thenReturn(tournamentTwo);

        TournamentDto tournamentDto = sut.createOrUpdateTournament(tournamentWithoutId, MatchGeneratorType.RANDOM);

        assertThat(tournamentDto)
                .isNotNull()
                .isEqualTo(tournamentTwo);
    }

    @Test
    public void deleteTournamentTest() {
        Long tournamentId = 1L;

        Mockito
                .when(tournamentService.deleteTournament(tournamentId))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity entity = sut.deleteTournament(tournamentId);

        assertThat(entity.getStatusCode())
                .isEqualTo(ResponseEntity.ok().build().getStatusCode());
    }
}
