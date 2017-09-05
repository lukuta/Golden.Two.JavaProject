package com.goldentwo.controller;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/leagues")
public class LeagueRestEndpoint {

    private LeagueService leagueService;

    @Autowired
    LeagueRestEndpoint(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public Set<LeagueDto> findAllLeagues() {
        return leagueService.findAllLeagues();
    }

    @GetMapping("/{id}")
    public LeagueDto findLeagueById(@PathVariable Long id) {
        return leagueService.findLeagueById(id);
    }

    @GetMapping("/find")
    public LeagueDto findLeagueByName(@RequestParam String name) {
        return leagueService.findLeagueByName(name);
    }

    @PostMapping
    public LeagueDto saveLeague(@RequestBody LeagueDto leagueDto) {
        return leagueService.saveLeague(leagueDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLeague(@PathVariable Long id) {
        return leagueService.deleteLeague(id);
    }

}
