package com.goldentwo.controller;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.TournamentException;
import com.goldentwo.model.Tournament;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public TournamentDto createTournament(@RequestBody TournamentDto tournamentDto) {
        return tournamentService.saveTournament(tournamentDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTournament(@PathVariable Long id) {
        return tournamentService.deleteTournament(id);
    }

    @ExceptionHandler(TournamentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(TournamentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", HttpStatus.NOT_FOUND.toString());

        return response;
    }
}
