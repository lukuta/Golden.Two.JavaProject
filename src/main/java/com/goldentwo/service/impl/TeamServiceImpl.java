package com.goldentwo.service.impl;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TeamStatisticsDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.Match;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public TeamDto findTeamById(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .orElseThrow(
                        () -> new NotFoundException("Team doesn't exists!"))
                .asDto();
    }

    @Override
    public TeamDto findTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(
                        () -> new NotFoundException("Team not found!"))
                .asDto();
    }

    @Override
    public List<TeamDto> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(Team::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDto saveTeam(TeamDto teamDto) {
        Set<Player> players = teamDto.getPlayers().stream()
                .map((playerDto -> Player.builder()
                        .id(playerDto.getId())
                        .name(playerDto.getName())
                        .surname(playerDto.getSurname())
                        .nickname(playerDto.getNickname())
                        .rankPoints(playerDto.getRankPoints())
                        .build()))
                .collect(Collectors.toSet());

        Team team = Team.builder()
                .name(teamDto.getName())
                .players(players)
                .rankPoints(teamDto.getRankPoints())
                .build();

        return teamRepository.saveAndFlush(team).asDto();
    }

    @Override
    public ResponseEntity deleteTeam(Long id) {
        teamRepository.delete(id);

        return ResponseEntity.ok().build();
    }

    @Override
    public TeamStatisticsDto findTeamStatistics(Long id) {
        TeamStatisticsDto teamStats = TeamStatisticsDto.builder().teamId(id).wins(0).defeats(0).build();

        Team team = findTeamById(id).asEntity();

        List<Match> teamMatches = matchRepository.findMatchesByTeamOneOrTeamTwoAndEnded(team, team, true)
                .orElse(new ArrayList<>());

        findTeamStatisticsInEachTeamMatch(teamStats, team, teamMatches);

        countWinsDefeatsRatio(teamStats);

        return teamStats;
    }

    private void countWinsDefeatsRatio(TeamStatisticsDto teamStats) {
        double wdRatio = ((double)teamStats.getWins()) / teamStats.getDefeats();
        teamStats.setWd((double) Math.round(wdRatio * 100) / 100);
    }

    private void findTeamStatisticsInEachTeamMatch(TeamStatisticsDto teamStats, Team team, List<Match> teamMatches) {
        for (Match match : teamMatches) {
            if ((match.getTeamOne().getId().equals(team.getId()) && match.getScoreTeamOne() == 16) || (match.getTeamTwo().getId().equals(team.getId()) && match.getScoreTeamTwo() == 16)) {
                teamStats.setWins(teamStats.getWins() + 1);
            } else {
                teamStats.setDefeats(teamStats.getDefeats() + 1);
            }
        }
    }
}
