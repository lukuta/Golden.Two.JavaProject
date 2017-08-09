package com.goldentwo.service.impl;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Override
    public TournamentDto findTournamentById(Long id) {
        return null;
    }

    @Override
    public List<TournamentDto> findAllTournaments() {
        return null;
    }

    @Override
    public TournamentDto saveTournament(TournamentDto tournamentDto) {
        return null;
    }

    @Override
    public ResponseEntity deleteTournament(Long id) {
        return null;
    }
}
