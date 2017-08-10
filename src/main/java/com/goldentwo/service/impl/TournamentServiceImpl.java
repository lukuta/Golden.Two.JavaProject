package com.goldentwo.service.impl;

import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.model.Member;
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
        Set<Member> members;

        if (tournamentDto.getMembers().size() > 0) {
            if (tournamentDto.getMembers().iterator().next() instanceof PlayerDto) {
                members = tournamentDto.getMembers()
                        .stream().map(memberDto -> {
                            PlayerDto playerDto = (PlayerDto) memberDto;
                            return Player.builder()
                                    .id(playerDto.getId())
                                    .name(playerDto.getName())
                                    .nickname(playerDto.getNickname())
                                    .surname(playerDto.getSurname())
                                    .build();
                        }).collect(Collectors.toSet());
            } else {

                members = tournamentDto.getMembers()
                        .stream().map(memberDto -> {
                            TeamDto teamDto = (TeamDto) memberDto;

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
            }
        } else {
            members = new HashSet<>();
        }

        return tournamentRepository.save(
                Tournament.builder()
                        .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .members(members)
                .build()
        ).asDto();
    }

    @Override
    public ResponseEntity deleteTournament(Long id) {
        return null;
    }
}
