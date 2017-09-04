package com.goldentwo.service;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TeamStatisticsDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamService {
    TeamDto findTeamById(Long id);

    TeamDto findTeamByName(String name);

    List<TeamDto> findAllTeams();

    TeamDto saveTeam(TeamDto team);

    ResponseEntity deleteTeam(Long id);

    TeamStatisticsDto findTeamStatistics(Long id);
}
