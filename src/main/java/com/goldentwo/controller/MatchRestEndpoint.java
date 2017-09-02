package com.goldentwo.controller;

import com.goldentwo.aspect.annotation.Monitored;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TurnDto;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "matches")
public class MatchRestEndpoint {

    private MatchService matchService;

    @Autowired
    public MatchRestEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }

    @Monitored
    @GetMapping
    public List<MatchDto> findAllMatches() {
        return matchService.findAllMatches();
    }

    @GetMapping(value = "/{id}")
    public MatchDto findMatchById(@PathVariable Long id) {
        return matchService.findMatchById(id);
    }

    @PostMapping
    public MatchDto saveMatch(@RequestBody @Valid MatchDto match) {
        return matchService.createMatch(match);
    }

    @PostMapping(value = "/{id}/turn")
    public TurnDto addTurn(@PathVariable Long id, @RequestBody TurnDto turnDto) {
        return matchService.addTurn(id, turnDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteMatch(@PathVariable Long id) {
        return matchService.deleteMatch(id);
    }
}
