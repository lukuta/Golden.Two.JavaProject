package com.goldentwo.controller;

import com.goldentwo.model.Match;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchRestEndpoint {

    private MatchService matchService;

    @Autowired
    public MatchRestEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping(value = "/")
    public Match match() {
        return matchService.getMatch();
    }

}
