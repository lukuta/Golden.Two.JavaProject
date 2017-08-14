package com.goldentwo.controller;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.TeamException;
import com.goldentwo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teams")
public class TeamRestEndpoint {
    private final TeamService teamService;

    @Autowired
    public TeamRestEndpoint(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.findAllTeams();
    }

    @GetMapping("{id}")
    public TeamDto findTeamById(@PathVariable Long id) {
        return teamService.findTeamById(id);
    }

    @GetMapping("/find")
    public TeamDto findTeamByName(@RequestParam String name) {
        return teamService.findTeamByName(name);
    }

    @PostMapping
    public TeamDto createTeam(@RequestBody @Valid TeamDto teamDto) {
        return teamService.saveTeam(teamDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTeam(@PathVariable Long id) {
        return teamService.deleteTeam(id);
    }

    @ExceptionHandler(TeamException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(TeamException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Message", ex.getMessage());

        return response;
    }
}
