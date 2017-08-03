package com.goldentwo.service.impl;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.exception.MatchException;
import com.goldentwo.model.Match;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public MatchDto findMatchById(Long id) {
        return Optional.ofNullable(matchRepository.findOne(id))
                .orElseThrow(
                        () -> new MatchException("Match doesn't exists!"))
                .asDto();
    }

    @Override
    public List<MatchDto> findAllMatches() {
        return matchRepository.findAll().stream()
                .map(Match::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDto saveMatch(MatchDto matchdto) {
        Match match = new Match(matchdto);

        return matchRepository.saveAndFlush(match).asDto();
    }

    @Override
    public ResponseEntity deleteMatch(Long id) {
        matchRepository.delete(id);

        return ResponseEntity.ok().build();
    }

}
