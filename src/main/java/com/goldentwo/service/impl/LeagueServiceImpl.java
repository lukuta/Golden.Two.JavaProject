package com.goldentwo.service.impl;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.model.League;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.service.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService {

    private LeagueRepository leagueRepository;

    LeagueServiceImpl(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Override
    public Set<LeagueDto> findAllLeagues() {
        return leagueRepository.findAll().stream().map(League::asDto).collect(Collectors.toSet());
    }

    @Override
    public LeagueDto findLeagueById(Long id) {
        return leagueRepository.findOne(id).asDto();
    }

    @Override
    public LeagueDto findLeagueByName(String name) {
        return null;
    }

    @Override
    public LeagueDto saveLeague(LeagueDto leagueDto) {
        return null;
    }

    @Override
    public ResponseEntity deleteLeague(Long id) {
        return null;
    }
}
