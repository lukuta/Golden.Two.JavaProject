package com.goldentwo.service;

import com.goldentwo.dto.MatchDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MatchService {
    MatchDto findMatchById(Long id);

    List<MatchDto> findAllMatches();

    MatchDto saveMatch(MatchDto match);

    ResponseEntity deleteMatch(Long id);
}
