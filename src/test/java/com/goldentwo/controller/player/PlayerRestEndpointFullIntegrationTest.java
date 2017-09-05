package com.goldentwo.controller.player;

import com.goldentwo.dto.PlayerDto;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerRestEndpointFullIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Before
    public void init() {
        playerRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    public void createPlayerShouldCreateTeams() {
        Player player = Player.builder()
                .id(1L)
                .name("Konrad")
                .surname("Klimczak")
                .nickname("klimeck")
                .rankPoints(100)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(PlayerDto.builder()
                        .name("Konrad")
                        .surname("Klimczak")
                        .nickname("klimeck")
                        .rankPoints(100)
                        .build())
                .when()
                .post("/api/v1/players")
                .then()
                .statusCode(200)
                .body("id", is(1));

        Team team = teamRepository.findByName("klimeck")
                .orElse(null);

        assertThat(team)
                .isNotNull();

        assertThat(team.getPlayers())
                .contains(player);
    }
}
