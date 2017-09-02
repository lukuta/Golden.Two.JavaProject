package com.goldentwo.service.impl;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.Tournament;
import com.goldentwo.model.TournamentMatch;
import com.goldentwo.repository.TournamentRepository;
import com.goldentwo.service.MatchesGeneratorService;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;

    private MatchesGeneratorService matchesGeneratorService;

    @Autowired
    TournamentServiceImpl(TournamentRepository tournamentRepository,
                          MatchesGeneratorService matchesGeneratorService) {
        this.tournamentRepository = tournamentRepository;
        this.matchesGeneratorService = matchesGeneratorService;
    }

    @Override
    public TournamentDto findTournamentById(Long id) {
        return Optional.ofNullable(tournamentRepository.findOne(id))
                .orElseThrow(() -> new NotFoundException("Tournament " + id + " doesn't exist!"))
                .asDto();
    }

    @Override
    public TournamentDto findTournamentByName(String name) {
        return tournamentRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tournament " + name + " doesn't exist!"))
                .asDto();
    }

    @Override
    public List<TournamentDto> findAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(Tournament::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public TournamentDto saveTournament(TournamentDto tournamentDto, MatchesGeneratorService.MatchGeneratorType type) {
        Set<Team> teams = getTeams(tournamentDto);
        Set<TournamentMatch> tournamentMatches = matchesGeneratorService.generateTournamentMatches(teams, type);

        return tournamentRepository.saveAndFlush(
                Tournament.builder()
                        .id(tournamentDto.getId())
                        .name(tournamentDto.getName())
                        .teams(teams)
                        .matches(tournamentMatches)
                        .build()
        ).asDto();
    }

    private Set<Team> getTeams(TournamentDto tournamentDto) {
        Set<Team> teams;
        if (tournamentDto.getTeams().isEmpty()) {

            teams = tournamentDto.getTeams().stream()
                    .map(teamDto -> {
                        Set<Player> players = teamDto.getPlayers().stream()
                                .map((playerDto -> Player.builder()
                                        .id(playerDto.getId())
                                        .name(playerDto.getName())
                                        .surname(playerDto.getSurname())
                                        .nickname(playerDto.getNickname())
                                        .rank(playerDto.getRank())
                                        .build()))
                                .collect(Collectors.toSet());

                        return Team.builder()
                                .id(teamDto.getId())
                                .name(teamDto.getName())
                                .players(players)
                                .rank(teamDto.getRank())
                                .build();
                    }).collect(Collectors.toSet());

        } else {
            teams = new HashSet<>();
        }
        return teams;
    }

    @Override
    public ResponseEntity deleteTournament(Long id) {
        tournamentRepository.delete(id);

        return ResponseEntity.noContent().build();
    }
}
