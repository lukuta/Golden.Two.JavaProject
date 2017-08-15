package com.goldentwo.service.tournament;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.TournamentException;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.Tournament;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TournamentRepository;
import com.goldentwo.service.impl.TournamentServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@ActiveProfiles("test")
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TournamentServiceImpl sut;

    private Team teamOne;
    private Team teamTwo;
    private Team teamThree;
    private TeamDto teamOneDto;
    private TeamDto teamTwoDto;
    private TeamDto teamThreeDto;

    private Tournament tournamentOne;
    private Tournament tournamentTwo;
    private Tournament tournamentWithoutId;
    private TournamentDto tournamentOneDto;
    private TournamentDto tournamentTwoDto;
    private TournamentDto tournamentWithoutIdDto;

    private Player playerOne;
    private Player playerTwo;

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

        teamThree = Team.builder()
                .id(3L)
                .name("GoldenTwo2")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();
        teamThreeDto = teamOne.asDto();

        tournamentOne = Tournament.builder()
                .id(1L)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamThree))
                .build();

        tournamentOneDto = tournamentOne.asDto();

        tournamentTwo = Tournament.builder()
                .id(2L)
                .name("PGL")
                .teams(Sets.newHashSet(teamTwo, teamOne))
                .build();

        tournamentTwoDto = tournamentTwo.asDto();

        tournamentWithoutId = Tournament.builder()
                .id(null)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamThree))
                .build();

        tournamentWithoutIdDto = tournamentWithoutId.asDto();
    }

    @Test
    public void findTournamentByIdWhenExist() {
        Long tournamentId = 1L;

        Mockito
                .when(tournamentRepository.findOne(tournamentId))
                .thenReturn(tournamentOne);

        TournamentDto tournamentFromSut = sut.findTournamentById(tournamentId);

        assertThat(tournamentFromSut)
                .isNotNull()
                .isEqualTo(tournamentOneDto);
    }

    @Test(expected = TournamentException.class)
    public void findTournamentByIdWhenNotExist() {
        Long tournamentId = 4L;

        Mockito
                .when(tournamentRepository.findOne(tournamentId))
                .thenReturn(null);

        sut.findTournamentById(tournamentId);
    }

    @Test
    public void findTournamentByNameWhenExist() {
        String tournamentName = "ELeague";

        Mockito
                .when(tournamentRepository.findByName(tournamentName))
                .thenReturn(Optional.ofNullable(tournamentOne));

        TournamentDto tournamentFromSut = sut.findTournamentByName(tournamentName);

        assertThat(tournamentFromSut)
                .isNotNull()
                .isEqualTo(tournamentOneDto);
    }

    @Test(expected = TournamentException.class)
    public void findTournamentByNameWhenNotExist() {
        String tournamentName = "ESL One";

        Mockito
                .when(tournamentRepository.findByName(tournamentName))
                .thenReturn(Optional.ofNullable(null));

        sut.findTournamentByName(tournamentName);
    }

    @Test
    public void findAllTournamentsTest() {
        List<Tournament> tournaments = Arrays.asList(tournamentOne, tournamentTwo);
        List<TournamentDto> expectedTournaments = Arrays.asList(tournamentOneDto, tournamentTwoDto);

        Mockito
                .when(tournamentRepository.findAll())
                .thenReturn(tournaments);

        List<TournamentDto> tournamentsFromSut = sut.findAllTournaments();

        assertThat(tournamentsFromSut)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedTournaments);
    }

    @Test
    public void saveTournamentTest() {
        Mockito
                .when(tournamentRepository.saveAndFlush(any()))
                .thenReturn(tournamentOne);
        Mockito
                .when(playerRepository.findByNickname(playerOne.getNickname()))
                .thenReturn(Optional.ofNullable(playerOne));
        Mockito
                .when(playerRepository.findByNickname(playerTwo.getNickname()))
                .thenReturn(Optional.ofNullable(playerTwo));

        TournamentDto savedTournamentFromSut = sut.saveTournament(tournamentWithoutIdDto);

        assertThat(savedTournamentFromSut)
                .isNotNull()
                .isEqualTo(tournamentOneDto);
    }

    @Test
    public void deleteTournamentTest() {
        ResponseEntity entity = sut.deleteTournament(1L);

        assertThat(entity.getStatusCode())
                .isEqualTo(ResponseEntity.noContent().build().getStatusCode());
    }
}
