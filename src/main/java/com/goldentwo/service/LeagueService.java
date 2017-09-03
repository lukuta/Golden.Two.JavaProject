package com.goldentwo.service;

import com.goldentwo.dto.LeagueDto;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface LeagueService {

   Set<LeagueDto> findAllLeagues();

   LeagueDto findLeagueById(Long id);

   LeagueDto findLeagueByName(String name);

   LeagueDto saveLeague(LeagueDto leagueDto);

   ResponseEntity deleteLeague(Long id);

}
