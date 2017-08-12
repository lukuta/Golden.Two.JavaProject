package com.goldentwo.service.impl;

import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.Tournament;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TournamentRepository;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;

    private PlayerRepository playerRepository;

    @Autowired
    TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public TournamentDto findTournamentById(Long id) {
        return tournamentRepository.findOne(id).asDto();
    }

    @Override
    public List<TournamentDto> findAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(Tournament::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public TournamentDto saveTournament(TournamentDto tournamentDto) {
        Set<Team> teams;

        if (tournamentDto.getTeams().size() > 0) {

                teams = tournamentDto.getTeams()
                        .stream().map(teamDto -> {
                            Set<Player> teamMates = new HashSet<>();
                            teamDto.getPlayerNicknames().forEach(
                                    nickname -> {
                                        teamMates.add(
                                                playerRepository
                                                        .findByNickname(nickname)
                                                        .orElseThrow(() -> new PlayerException("Player not found!"))
                                        );
                                    }
                            );

                            return Team.builder()
                                    .id(teamDto.getId())
                                    .name(teamDto.getName())
                                    .players(teamMates)
                                    .build();
                        }).collect(Collectors.toSet());

        } else {
            teams = new HashSet<>();
        }

        return tournamentRepository.save(
                Tournament.builder()
                        .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .members(teams)
                .build()
        ).asDto();
    }

    @Override
    public ResponseEntity deleteTournament(Long id) {
        tournamentRepository.delete(id);

        return ResponseEntity.ok().build();
    }
}
