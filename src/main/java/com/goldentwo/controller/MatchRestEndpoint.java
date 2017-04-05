package com.goldentwo.controller;

import com.goldentwo.model.Match;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/match")
public class MatchRestEndpoint {

    private MatchService matchService;

    @Autowired
    public MatchRestEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping(value = "")
    public List<Match> findAllMatches() {
        return matchService.findAllMatches();
    }

    @GetMapping(value = "/{id}")
    public Match findMatchById(@PathVariable Long id) {
        return matchService.findMatchById(id);
    }

    @PostMapping(value = "")
    public Match saveMatch(@RequestBody Match match) {
        return matchService.saveMatch(match);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }

}
