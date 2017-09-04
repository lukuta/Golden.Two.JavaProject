package com.goldentwo.service.player;

import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.PlayerStatisticsDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.*;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.TeamService;
import com.goldentwo.service.impl.PlayerServiceImpl;
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

@ActiveProfiles("test")
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private PlayerServiceImpl sut;

    private Player playerOne;
    private Player playerTwo;
    private Player unsavedPlayer;

    private PlayerDto playerOneDto;
    private PlayerDto playerTwoDto;
    private PlayerDto unsavedPlayerDto;

    private TeamDto unsavedPlayerTeam;
    private TeamDto savedPlayerTeam;

    private Team teamOne;

    private Match matchOne;

    private PlayerStatisticsDto playerStatistics;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        playerOne = Player.builder().id(1L).name("Konrad").surname("Klimczak")
                .nickname("klimeck").rankPoints(20).build();

        playerOneDto = playerOne.asDto();

        playerTwo = Player.builder().id(2L).name("Łukasz").surname("Kuta")
                .nickname("qtek").rankPoints(40).build();

        playerTwoDto = playerTwo.asDto();

        unsavedPlayer = Player.builder().name("Łukasz").surname("Kuta")
                .nickname("qtek").rankPoints(40).build();

        unsavedPlayerDto = unsavedPlayer.asDto();

        unsavedPlayerTeam = TeamDto.builder().name("qtek").rankPoints(40).players(Sets.newHashSet(playerTwoDto))
                .build();

        teamOne = Team.builder().id(2L).name("klimeck").players(Sets.newHashSet(playerOne))
                .rankPoints(playerOne.getRankPoints()).build();

        Map<String, Integer> turnOneKills = new HashMap<>();
        turnOneKills.put("qtek", 1);
        Turn turnOne = Turn.builder().kills(turnOneKills).winner(1L).winType(TurnWinType.DEFUSE).no(1)
                .deaths(Sets.newHashSet("klimeck")).build();

        Map<String, Integer> turnTwoKills = new HashMap<>();
        turnTwoKills.put("klimeck", 1);
        Turn turnTwo = Turn.builder().kills(turnTwoKills).winner(2L).winType(TurnWinType.ALL_KILLED).no(2)
                .deaths(Sets.newHashSet("qtek")).build();

        matchOne = Match.builder().ended(false).teamOne(teamOne).teamTwo(unsavedPlayerTeam.asEntity())
                .turns(Sets.newHashSet(turnOne, turnTwo)).scoreTeamTwo(1).scoreTeamOne(1).build();

        playerStatistics = PlayerStatisticsDto.builder().playerId(playerTwo.getId())
                .kills(1).deaths(1).kd(1.0).build();
    }

    @Test
    public void findAllPlayersTest() {
        Mockito
                .when(playerRepository.findAll())
                .thenReturn(Arrays.asList(playerOne, playerTwo));

        List<PlayerDto> playersFromSut = sut.findAllPlayers();

        assertThat(playersFromSut)
                .isNotEmpty()
                .isEqualTo(Arrays.asList(playerOneDto, playerTwoDto));
    }

    @Test
    public void findPlayerByIdWhenExist() {
        Long playerId = 1L;

        Mockito
                .when(playerRepository.findOne(playerId))
                .thenReturn(playerOne);

        PlayerDto playerFromSut = sut.findPlayerById(playerId);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerOneDto);
    }

    @Test(expected = NotFoundException.class)
    public void findPlayerByIdWhenNotExist() {
        Long playerId = 4L;

        Mockito
                .when(playerRepository.findOne(playerId))
                .thenReturn(null);

        sut.findPlayerById(playerId);
    }

    @Test
    public void findPlayerByNameWhenExist() {
        String playerNickname = "qtek";

        Mockito
                .when(playerRepository.findByNickname(playerNickname))
                .thenReturn(Optional.ofNullable(playerTwo));

        PlayerDto playerFromSut = sut.findPlayerByNickname(playerNickname);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerTwoDto);
    }

    @Test(expected = NotFoundException.class)
    public void findPlayerByNameWhenNotExist() {
        String playerNickname = "pasha";

        Mockito
                .when(playerRepository.findByNickname(playerNickname))
                .thenReturn(Optional.empty());

        sut.findPlayerByNickname(playerNickname);
    }

    @Test
    public void savePlayerTest() {
        Mockito
                .when(playerRepository.saveAndFlush(unsavedPlayer))
                .thenReturn(playerTwo);

        Mockito
                .when(teamService.saveTeam(unsavedPlayerTeam))
                .thenReturn(unsavedPlayerTeam);

        PlayerDto playerFromSut = sut.savePlayer(unsavedPlayerDto);

        assertThat(playerFromSut)
                .isNotNull()
                .isEqualTo(playerTwoDto);
    }

    @Test
    public void deletePlayerTest() {
        ResponseEntity responseFromSut = sut.deletePlayer(1L);

        assertThat(responseFromSut)
                .isEqualTo(ResponseEntity.noContent().build());
    }

    @Test(expected = NotFoundException.class)
    public void findPlayerStatisticsWhenPlayerHasAnyTeam() {
        Long playerId = 1L;

        Mockito
                .when(playerRepository.findOne(playerId))
                .thenReturn(playerOne);

        Mockito
                .when(teamRepository.findTeamsByPlayers(playerOne))
                .thenReturn(Optional.empty());

        sut.findPlayerStatistics(playerId);
    }

    @Test
    public void findPlayerStatisticsWhenPlayerHasOneTeamAtLeast() {
        Long playerId = 2L;

        Mockito
                .when(playerRepository.findOne(playerId))
                .thenReturn(playerTwo);

        Mockito
                .when(teamRepository.findTeamsByPlayers(playerTwo))
                .thenReturn(Optional.of(Collections.singletonList(unsavedPlayerTeam.asEntity())));

        Mockito
                .when(matchRepository.findMatchesByTeamOneOrTeamTwo(unsavedPlayerTeam.asEntity(), unsavedPlayerTeam.asEntity()))
                .thenReturn(Optional.of(Collections.singletonList(matchOne)));

        PlayerStatisticsDto statsFromSut = sut.findPlayerStatistics(playerId);

        assertThat(statsFromSut)
                .isNotNull()
                .isEqualTo(playerStatistics);
    }
}
