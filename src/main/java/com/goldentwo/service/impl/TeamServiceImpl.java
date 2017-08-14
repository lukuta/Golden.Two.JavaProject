package com.goldentwo.service.impl;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.exception.TeamException;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;


    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public TeamDto findTeamById(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .orElseThrow(
                        () -> new TeamException("Team doesn't exists!"))
                .asDto();
    }

    @Override
    public TeamDto findTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(
                        () -> new TeamException("Team not found!"))
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
        Set<Player> teamMates = new HashSet<>();
        teamDto.getPlayerNicknames().forEach(
                nickname -> {
                    teamMates.add(
                            playerRepository
                                    .findByNickname(nickname)
                                    .orElseThrow(() -> new PlayerException("Player not found!"))
                    );
                }
        );

        Team team = Team.builder()
                .name(teamDto.getName())
                .players(teamMates)
                .build();

        return teamRepository.saveAndFlush(team).asDto();
    }

    @Override
    public ResponseEntity deleteTeam(Long id) {
        teamRepository.delete(id);

        return ResponseEntity.ok().build();
    }
}
