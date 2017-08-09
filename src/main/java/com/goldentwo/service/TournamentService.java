package com.goldentwo.service;

import com.goldentwo.dto.TournamentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TournamentService {

    TournamentDto findTournamentById(Long id);

    List<TournamentDto> findAllTournaments();

    TournamentDto saveTournament(TournamentDto tournamentDto);

    ResponseEntity deleteTournament(Long id);

}
