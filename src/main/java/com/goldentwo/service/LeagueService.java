package com.goldentwo.service;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.model.League;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LeagueService {

   Set<League> findAllLeagues();

   League findLeagueById(Long id);

   League findLeagueByName(String name);

   League saveLeague(LeagueDto leagueDto);

   ResponseEntity deleteLeague(Long id);

}
