package com.goldentwo.controller.league;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.model.League;
import com.goldentwo.model.Team;
import com.goldentwo.repository.*;
import com.goldentwo.service.PlayerService;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LeagueRestEndpointFullIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    RoundRepository roundRepository;

    @Before
    public void init() {
        leagueRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();

        for (int i = 1; i < 4; i++) {
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

    @Test
    public void saveLeagueTest() {
        String leagueName = "LEAGUE";
        Set<TeamDto> teams = teamRepository.findAll().stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());

        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                body(LeagueDto.builder().name(leagueName).teams(teams).build()).
                when().
                post("/api/v1/leagues").
                then().
                statusCode(200)
                .body("name", is(leagueName))
                .body("rounds", hasSize(3));
    }

    @Test
    public void deleteLeagueTest() {
        String leagueName = "LEAGUE";
        Set<Team> teams = new HashSet<>(teamRepository.findAll());

        Long leagueId = leagueRepository.save(League.builder().name(leagueName).teams(teams).build()).getId();

        given().
                port(port).
                contentType(ContentType.JSON).
                header("Accept", "application/json").
                when().
                delete("/api/v1/leagues/" + leagueId).
                then().
                statusCode(204);

        League league = leagueRepository.findOne(leagueId);

        assertThat(league)
                .isNull();
    }

}
