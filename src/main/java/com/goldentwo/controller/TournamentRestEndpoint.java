package com.goldentwo.controller;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.service.MatchesGeneratorService;
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

    @GetMapping(value = "/find")
    public TournamentDto findTournamentByName(@RequestParam String name) {
        return tournamentService.findTournamentByName(name);
    }

    //TODO need to add @InitBinder method for parsing enum param
    @PostMapping
    public TournamentDto createOrUpdateTournament(@RequestBody TournamentDto tournamentDto,
                                                  @RequestParam(name = "type", required = false) MatchesGeneratorService.MatchGeneratorType type) {
        if (type == null) {
            type = MatchesGeneratorService.MatchGeneratorType.RANDOM;
        }

        return tournamentService.saveTournament(tournamentDto, type);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTournament(@PathVariable Long id) {
        return tournamentService.deleteTournament(id);
    }
}
