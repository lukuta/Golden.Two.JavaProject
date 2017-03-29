package com.goldentwo.service;

import com.goldentwo.model.Match;
import org.springframework.stereotype.Service;

@Service
public class MatchService {

    public Match getMatch() {
        return Match.builder().id(1L).build();
    }

}
