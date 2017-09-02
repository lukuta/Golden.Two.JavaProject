package com.goldentwo.service;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TurnDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {
    MatchDto findMatchById(Long id);

    List<MatchDto> findAllMatches();

    MatchDto createMatch(MatchDto match);

    MatchDto updateMatch(MatchDto match);

    TurnDto addTurn(Long matchId, TurnDto turnDto);

    ResponseEntity deleteMatch(Long id);
}
