package com.goldentwo.controller.tournament;

import com.goldentwo.Janitor;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.model.Team;
import com.goldentwo.model.Tournament;
import com.goldentwo.repository.*;
import com.goldentwo.service.PlayerService;
import com.jayway.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TournamentRestEndpointFullIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private Janitor janitor;


    @Before
    public void init() {
        tearDown();
        for (int i = 1; i < 9; i++) {
            playerService.savePlayer(
                    PlayerDto.builder()
                            .name("name" + i)
                            .surname("surname" + i)
                            .nickname("nickname" + i)
                            .rankPoints(i)
                            .build()
            );
        }
    }

    @After
    public void tearDown() {
        janitor.clearRepos();
    }

    @Test
    public void createTournamentUsingRandomGenerator() {
        String tournamentName = "TOURNAMENT";
        Set<TeamDto> teams = teamRepository.findAll().stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());
        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                body(TournamentDto.builder().name(tournamentName).teams(teams).build()).
                when().
                post("/api/v1/tournaments?type=RANDOM").
                then().
                statusCode(200);

        Optional<Tournament> foundTournamentOptional = tournamentRepository.findByName(tournamentName);

        assertThat(foundTournamentOptional.isPresent())
                .isTrue();
        assertThat(foundTournamentOptional.get().getName())
                .isEqualToIgnoringCase(tournamentName);
    }

    @Test
    public void createTournamentUsingCompetitorRankGenerator() {
        String tournamentName = "TOURNAMENT";
        Set<TeamDto> teams = teamRepository.findAll().stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());
        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                body(TournamentDto.builder().name(tournamentName).teams(teams).build()).
                when().
                post("/api/v1/tournaments?type=COMPETITOR_RANK").
                then().
                statusCode(200);

        Optional<Tournament> foundTournamentOptional = tournamentRepository.findByName(tournamentName);

        assertThat(foundTournamentOptional.isPresent())
                .isTrue();
        assertThat(foundTournamentOptional.get().getName())
                .isEqualToIgnoringCase(tournamentName);
    }

    @Test
    public void createTournamentUsingBasketGenerator() {
        String tournamentName = "TOURNAMENT";
        Set<TeamDto> teams = teamRepository.findAll().stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());

        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                body(TournamentDto.builder().name(tournamentName).teams(teams).build()).
                when().
                post("/api/v1/tournaments?type=BASKETS").
                then().
                statusCode(200);

        Optional<Tournament> foundTournamentOptional = tournamentRepository.findByName(tournamentName);

        assertThat(foundTournamentOptional.isPresent())
                .isTrue();
        assertThat(foundTournamentOptional.get().getName())
                .isEqualToIgnoringCase(tournamentName);
    }

    @Test
    public void givenOddTeamsSizeShouldThrowBadRequestException() {
        String tournamentName = "TOURNAMENT";
        Set<TeamDto> teams = teamRepository.findAll().stream()
                .filter(team -> team.getId() % 3 == 0)
                .map(Team::asDto)
                .collect(Collectors.toSet());

        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                body(TournamentDto.builder().name(tournamentName).teams(teams).build()).
                when().
                post("/api/v1/tournaments?type=BASKETS").
                then().
                statusCode(400);
    }
}
