package com.goldentwo.controller.player;

import com.goldentwo.controller.PlayerRestEndpoint;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.PlayerStatisticsDto;
import com.goldentwo.service.PlayerService;
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

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class PlayerRestEndpointTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerRestEndpoint sut;

    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private PlayerDto unsavedPlayer;
    private PlayerStatisticsDto playerStatistics;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        playerOne = PlayerDto.builder().id(1L).name("Konrad").surname("Klimczak")
                .nickname("klimeck").rankPoints(20).build();

        playerTwo = PlayerDto.builder().id(2L).name("Łukasz").surname("Kuta")
                .nickname("qtek").rankPoints(40).build();

        unsavedPlayer = PlayerDto.builder().name("Łukasz").surname("Kuta")
                .nickname("qtek").rankPoints(40).build();

        playerStatistics = PlayerStatisticsDto.builder().playerId(playerTwo.getId())
                .kills(1).deaths(1).kd(1.0).build();
    }

    @Test
    public void findAllPlayersTest() {
        Mockito
                .when(playerService.findAllPlayers())
                .thenReturn(Arrays.asList(playerOne, playerTwo));

        List<PlayerDto> playersFromSut = sut.findAllPlayers();

        assertThat(playersFromSut)
                .isNotEmpty()
                .isEqualTo(Arrays.asList(playerOne, playerTwo));
    }

    @Test
    public void findPlayerByIdTest() {
        Long playerId = 1L;

        Mockito
                .when(playerService.findPlayerById(playerId))
                .thenReturn(playerOne);

        PlayerDto playerFromSut = sut.findPlayerById(playerId);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerOne);
    }

    @Test
    public void findPlayerByNicknameTest() {
        String playerNickname = "qtek";

        Mockito
                .when(playerService.findPlayerByNickname(playerNickname))
                .thenReturn(playerTwo);

        PlayerDto playerFromSut = sut.findPlayerByNickname(playerNickname);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerTwo);
    }

    @Test
    public void savePlayerTest() {
        Mockito
                .when(playerService.savePlayer(unsavedPlayer))
                .thenReturn(playerTwo);

        PlayerDto playerFromSut = sut.savePlayer(unsavedPlayer);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerTwo);
    }

    @Test
    public void deletePlayerTest() {
        Long id = 1L;
        ResponseEntity expectedResponse = ResponseEntity.noContent().build();

        Mockito
                .when(playerService.deletePlayer(id))
                .thenReturn(expectedResponse);

        ResponseEntity responseFromSut = sut.deletePlayer(id);

        assertThat(responseFromSut)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    public void findPlayerStatisticsTest() {
        Long playerId = 2L;

        Mockito
                .when(playerService.findPlayerStatistics(playerId))
                .thenReturn(playerStatistics);

        PlayerStatisticsDto statsFromSut = sut.findPlayerStatistics(playerId);

        assertThat(statsFromSut)
                .isNotNull()
                .isEqualTo(playerStatistics);
    }
}
