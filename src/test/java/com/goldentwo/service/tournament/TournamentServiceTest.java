package com.goldentwo.service.tournament;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.*;
import com.goldentwo.repository.TournamentMatchRepository;
import com.goldentwo.repository.TournamentRepository;
import com.goldentwo.service.MatchService;
import com.goldentwo.service.MatchesGeneratorService;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@ActiveProfiles("test")
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentMatchRepository tournamentMatchRepository;

    @Mock
    private MatchesGeneratorService matchesGeneratorService;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private TournamentServiceImpl sut;

    private TournamentMatch tournamentMatchOne;

    private Tournament tournamentOne;
    private Tournament tournamentTwo;
    private TournamentDto tournamentOneDto;
    private TournamentDto tournamentTwoDto;
    private TournamentDto tournamentWithoutIdDto;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

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
                .players(Sets.newHashSet(playerOne, playerTwo, playerThree, playerFour, playerFive))
                .build();

        Team teamThree = Team.builder()
                .id(3L)
                .name("GoldenTwo2")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build();

        tournamentMatchOne = TournamentMatch.builder()
                .id(1L)
                .match(Match.builder()
                        .id(1L).ended(false).scoreTeamOne(1).scoreTeamTwo(2)
                        .teamOne(teamOne).teamTwo(teamTwo).build())
                .nextRoundId(null)
                .round(1)
                .build();

        tournamentOne = Tournament.builder()
                .id(1L)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamThree))
                .matches(Sets.newHashSet(tournamentMatchOne))
                .build();

        tournamentOneDto = tournamentOne.asDto();

        tournamentTwo = Tournament.builder()
                .id(2L)
                .name("PGL")
                .teams(Sets.newHashSet(teamTwo, teamOne))
                .matches(Sets.newHashSet(tournamentMatchOne))
                .build();

        tournamentTwoDto = tournamentTwo.asDto();

        Tournament tournamentWithoutId = Tournament.builder()
                .id(null)
                .name("ELeague")
                .teams(Sets.newHashSet(teamOne, teamThree))
                .matches(Sets.newHashSet(tournamentMatchOne))
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

    @Test(expected = NotFoundException.class)
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

    @Test(expected = NotFoundException.class)
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
        Set<TournamentMatch> matches = tournamentOne.getMatches();

        mockSaveTournamentComponents(matches);

        TournamentDto savedTournamentFromSut =
                sut.saveTournament(tournamentWithoutIdDto, MatchesGeneratorService.MatchGeneratorType.RANDOM);

        assertThat(savedTournamentFromSut)
                .isNotNull()
                .isEqualTo(tournamentOneDto);
    }

    private void mockSaveTournamentComponents(Set<TournamentMatch> matches) {
        Mockito
                .when(tournamentRepository.saveAndFlush(any()))
                .thenReturn(tournamentOne);

        Mockito
                .when(matchService.saveMatch(any()))
                .thenReturn(tournamentMatchOne.asDto().getMatch());

        Mockito
                .when(tournamentMatchRepository.save((TournamentMatch) any()))
                .thenReturn(tournamentMatchOne);

        Mockito
                .when(matchesGeneratorService.generateTournamentMatches(any(), any()))
                .thenReturn(matches);
    }

    @Test
    public void deleteTournamentTest() {
        ResponseEntity entity = sut.deleteTournament(1L);

        assertThat(entity.getStatusCode())
                .isEqualTo(ResponseEntity.noContent().build().getStatusCode());
    }
}
