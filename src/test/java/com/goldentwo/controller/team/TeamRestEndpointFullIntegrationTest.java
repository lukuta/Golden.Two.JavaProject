package com.goldentwo.controller.team;

import com.goldentwo.Janitor;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.google.common.collect.Sets;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamRestEndpointFullIntegrationTest {

    private Long teamId;

    @LocalServerPort
    private int port;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private Janitor janitor;

    @Before
    public void init() {
        janitor.clearRepos();

        Player playerOne = playerRepository.save(Player.builder()
                .name("Konrad")
                .surname("Klimczak")
                .nickname("klimeck")
                .rankPoints(100)
                .build());

        Player playerTwo = playerRepository.save(Player.builder()
                .name("Łukasz")
                .surname("Kuta")
                .nickname("qtek")
                .rankPoints(100)
                .build());

        teamId = teamRepository.save(Team.builder()
                .name("CRUDTeams")
                .rankPoints(200)
                .players(Sets.newHashSet(playerOne, playerTwo))
                .build()).getId();
    }

    @Test
    public void findTeamTest() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .when()
                .delete("/api/v1/teams/" + teamId)
                .then()
                .statusCode(200);

        Team team = teamRepository.findByName("CRUDTeams").orElse(null);

        assertThat(team)
                .isNull();
    }

}
