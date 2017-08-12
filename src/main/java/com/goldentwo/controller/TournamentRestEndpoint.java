package com.goldentwo.controller;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{id}")
    public TournamentDto findTournamentById(@PathVariable Long id) {
        return tournamentService.findTournamentById(id);
    }

    @PostMapping
    public TournamentDto createTournament(@RequestBody TournamentDto tournamentDto) {
        return tournamentService.saveTournament(tournamentDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTournament(@PathVariable Long id) {
        return tournamentService.deleteTournament(id);
    }

}
