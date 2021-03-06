package com.goldentwo.controller.league;

import com.goldentwo.Janitor;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    private PlayerService playerService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private Janitor janitor;

    @Before
    public void init() {
        janitor.clearRepos();
        for (int i = 1; i < 5; i++) {
            playerService.savePlayer(
                    PlayerDto.builder()
                            .name("name" + i + 100)
                            .surname("surname" + i)
                            .nickname("nickname" + i + 100)
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

}
