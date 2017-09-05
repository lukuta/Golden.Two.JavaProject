package com.goldentwo.service;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.MatchSummaryDto;
import com.goldentwo.dto.TurnDto;
import com.goldentwo.model.Match;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {
    MatchDto findMatchById(Long id);

    List<MatchDto> findAllMatches();

    MatchDto createMatch(MatchDto match);

    MatchDto updateMatch(MatchDto match);

    TurnDto addTurn(Long matchId, TurnDto turnDto);

    MatchSummaryDto getMatchSummary(Long matchId);

    ResponseEntity deleteMatch(Long id);

    List<MatchDto> findMatchesBySpecificFilter(Specification<Match> spec);
}
