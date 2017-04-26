package com.goldentwo.service.impl;

import com.goldentwo.exception.MatchException;
import com.goldentwo.model.Match;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match findMatchById(Long id) {
        if (!matchRepository.exists(id)) {
            throw new MatchException("There is no match with given id");
        }
        return matchRepository.findOne(id);
    }

    @Override
    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match saveMatch(Match match) {
        return matchRepository.saveAndFlush(match);
    }

    @Override
    public ResponseEntity<?> deleteMatch(Long id) {
        matchRepository.delete(id);

        return ResponseEntity.ok().build();
    }

}
