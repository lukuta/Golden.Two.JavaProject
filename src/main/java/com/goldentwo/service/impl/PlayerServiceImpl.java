package com.goldentwo.service.impl;

import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.PlayerStatisticsDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.Match;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.Turn;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.PlayerService;
import com.goldentwo.service.TeamService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;
    private TeamService teamService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, MatchRepository matchRepository, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.teamService = teamService;
    }

    @Override
    public PlayerDto findPlayerById(Long id) {
        return Optional.ofNullable(playerRepository.findOne(id))
                .orElseThrow(
                        () -> new NotFoundException("Player doesn't exists!"))
                .asDto();
    }

    @Override
    public PlayerDto findPlayerByNickname(String nickname) {
        return playerRepository
                .findByNickname(nickname)
                .orElseThrow(
                        () -> new NotFoundException("There is no player with given nickname")
                ).asDto();
    }

    //    TODO: find usage for cacheable
//    @Cacheable(CacheConstants.PLAYERS_CACHE)
    @Override
    public List<PlayerDto> findAllPlayers() {
        return playerRepository.findAll().stream()
                .map(Player::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDto savePlayer(PlayerDto playerDto) {
        Player player = new Player(playerDto);

        PlayerDto savedPlayer = playerRepository.saveAndFlush(player).asDto();

        teamService.saveTeam(TeamDto.builder()
                .name(savedPlayer.getNickname())
                .players(Sets.newHashSet(savedPlayer))
                .rankPoints(playerDto.getRankPoints())
                .build());

        return savedPlayer;
    }

    @Override
    public ResponseEntity deletePlayer(Long id) {
        playerRepository.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public PlayerStatisticsDto findPlayerStatistics(Long id) {
        PlayerStatisticsDto playerStatistics =
                PlayerStatisticsDto.builder().playerId(id).kills(0).deaths(0).build();

        Player player = findPlayerById(id).asEntity();

        List<Team> playerTeams = teamRepository.findTeamsByPlayers(player)
                .orElseThrow(() -> new NotFoundException("Player doesn't have team"));

        List<Match> playerMatches = findMatchesInWhichPlayPlayerTeams(playerTeams);

        findPlayerStatisticsInEachMatchTurn(playerStatistics, player, playerMatches);

        countPlayerKillsDeathsRatio(playerStatistics);

        return playerStatistics;
    }

    private void countPlayerKillsDeathsRatio(PlayerStatisticsDto playerStatistics) {
        double kdRatio = ((double)playerStatistics.getKills()) / playerStatistics.getDeaths();

        playerStatistics.setKd((double) Math.round(kdRatio * 100) / 100);
    }

    private void findPlayerStatisticsInEachMatchTurn(PlayerStatisticsDto playerStatistics, Player player, List<Match> playerMatches) {
        for (Match match : playerMatches) {
            for (Turn turn : match.getTurns()) {
                Integer kills = turn.getKills().get(player.getNickname());

                if (kills != null) {
                    playerStatistics.setKills(playerStatistics.getKills() + kills);
                }

                if (turn.getDeaths().contains(player.getNickname())) {
                    playerStatistics.setDeaths(playerStatistics.getDeaths() + 1);
                }
            }
        }
    }

    private List<Match> findMatchesInWhichPlayPlayerTeams(List<Team> playerTeams) {
        List<Match> playerMatches = new ArrayList<>();

        for (Team team : playerTeams) {
            playerMatches.addAll(matchRepository.findMatchesByTeamOneOrTeamTwo(team, team).orElse(new ArrayList<>()));
        }
        return playerMatches;
    }
}
