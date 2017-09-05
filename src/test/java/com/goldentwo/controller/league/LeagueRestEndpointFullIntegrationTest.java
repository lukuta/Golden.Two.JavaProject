package com.goldentwo.controller.league;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
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

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static com.jayway.restassured.RestAssured.given;

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

    @Before
    public void init() {
        leagueRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();

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
                .body("rounds", hasSize(7));
    }

}
