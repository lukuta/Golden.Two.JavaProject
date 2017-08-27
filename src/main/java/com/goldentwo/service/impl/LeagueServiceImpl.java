package com.goldentwo.service.impl;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.League;
import com.goldentwo.model.Match;
import com.goldentwo.model.Round;
import com.goldentwo.model.Team;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService {

    private LeagueRepository leagueRepository;

    @Autowired
    LeagueServiceImpl(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Override
    public Set<LeagueDto> findAllLeagues() {
        return leagueRepository.findAll().stream().map(League::asDto).collect(Collectors.toSet());
    }

    @Override
    public LeagueDto findLeagueById(Long id) {
        return Optional.ofNullable(leagueRepository.findOne(id))
                .orElseThrow(() -> new NotFoundException("League " + id + " not found"))
                .asDto();
    }

    @Override
    public LeagueDto findLeagueByName(String name) {
        return leagueRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("League " + name + " not found"))
                .asDto();
    }

    @Override
    public LeagueDto saveLeague(LeagueDto leagueDto) {
        leagueDto.setRounds(new HashSet<>());

        Set<Round> rounds = new HashSet<>();
        List<Team> teamList = leagueDto.getTeams().stream()
                .map(TeamDto::asEntity)
                .sorted(Comparator.comparingLong(Team::getId))
                .collect(Collectors.toList());

        Deque<Team> teams = new LinkedList<>();

        teams.addAll(teamList);

        Team lastTeam = teams.removeLast();

        for (int i = 0; i < leagueDto.getTeams().size()-1; i++) {
            Round round = Round.builder().no(i+1).matches(new HashSet<>()).build();
            List<Team> teams1 = new ArrayList<>();
            teams1.addAll(teams);

            for (int j = 0; j < leagueDto.getTeams().size()/2; j++) {
                Match match = new Match();

                if (j == 0) {
                    if (i % 2 == 0) {
                        match.setTeamOne(lastTeam);
                        match.setTeamTwo(teams.getFirst());
                    } else {
                        match.setTeamOne(teams.getFirst());
                        match.setTeamTwo(lastTeam);
                    }
                } else {
                    match.setTeamOne(teams1.get(j));
                    match.setTeamTwo(teams1.get(teams1.size()-1));
                }

                round.getMatches().add(match);
            }
            teams.addFirst(teams.removeLast());
            rounds.add(round);
        }


        League league = leagueDto.asEntity();
        league.setRounds(rounds);

        System.out.println(league);


        return leagueRepository.save(league).asDto();
    }

    @Override
    public ResponseEntity deleteLeague(Long id) {
        leagueRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
