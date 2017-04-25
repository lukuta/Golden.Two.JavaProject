package com.goldentwo.service;

import com.goldentwo.model.Match;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {
    Match findMatchById(Long id);

    List<Match> findAllMatches();

    Match saveMatch(Match match);

    ResponseEntity<?> deleteMatch(Long id);
}
