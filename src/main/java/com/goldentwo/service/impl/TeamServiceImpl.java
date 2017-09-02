package com.goldentwo.service.impl;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
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
                        .rank(playerDto.getRank())
                        .build()))
                .collect(Collectors.toSet());

        Team team = Team.builder()
                .name(teamDto.getName())
                .players(players)
                .rank(teamDto.getRank())
                .build();

        return teamRepository.saveAndFlush(team).asDto();
    }

    @Override
    public ResponseEntity deleteTeam(Long id) {
        teamRepository.delete(id);

        return ResponseEntity.ok().build();
    }
}
