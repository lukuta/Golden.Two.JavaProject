package com.goldentwo.service.impl;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.model.League;
import com.goldentwo.service.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeagueServiceImpl implements LeagueService {
    
    @Override
    public Set<League> findAllLeagues() {
        return null;
    }

    @Override
    public League findLeagueById(Long id) {
        return null;
    }

    @Override
    public League findLeagueByName(String name) {
        return null;
    }

    @Override
    public League saveLeague(LeagueDto leagueDto) {
        return null;
    }

    @Override
    public ResponseEntity deleteLeague(Long id) {
        return null;
    }
}
