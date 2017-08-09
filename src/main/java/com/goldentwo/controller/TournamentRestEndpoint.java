package com.goldentwo.controller;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentRestEndpoint {

    private final TournamentService tournamentService;

    @Autowired
    TournamentRestEndpoint(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<TournamentDto> findAllTournaments() {
        return tournamentService.findAllTournaments();
    }

}
