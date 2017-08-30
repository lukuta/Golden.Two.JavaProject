package com.goldentwo.service.impl;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.exception.BadRequestException;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.exception.TournamentException;
import com.goldentwo.model.*;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TournamentMatchRepository;
import com.goldentwo.repository.TournamentRepository;
import com.goldentwo.service.MatchService;
import com.goldentwo.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private MatchService matchService;

    private TournamentRepository tournamentRepository;

    private PlayerRepository playerRepository;

    private TournamentMatchRepository tournamentMatchRepository;

    @Autowired
    TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository, TournamentMatchRepository tournamentMatchRepository, MatchService matchService) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.tournamentMatchRepository = tournamentMatchRepository;
        this.matchService = matchService;
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
    public TournamentDto saveTournament(TournamentDto tournamentDto) {
        Set<Team> teams = getTeams(tournamentDto);
        Set<TournamentMatch> tournamentMatches = createTournamentMatches(teams);

        return tournamentRepository.saveAndFlush(
                Tournament.builder()
                        .id(tournamentDto.getId())
                        .name(tournamentDto.getName())
                        .teams(teams)
                        .matches(tournamentMatches)
                        .build()
        ).asDto();
    }

    private Set<TournamentMatch> createTournamentMatches(Set<Team> teams) {
        Set<TournamentMatch> tournamentMatches = new HashSet<>();
        if (teams.size() > 1 && (teams.size() & (teams.size() - 1)) == 0) {

            List<Team> teamsToAssign = new ArrayList<>(teams);

            Collections.shuffle(teamsToAssign);

            List<Match> matches = new ArrayList<>();

            for (int i = 0; i < teamsToAssign.size(); i += 2) {
                MatchDto matchDto = matchService.saveMatch(MatchDto.builder()
                        .teamOne(teamsToAssign.get(i).asDto())
                        .teamTwo(teamsToAssign.get(i + 1).asDto())
                        .build());

                matches.add(
                        Match.builder()
                                .id(matchDto.getId())
                                .teamOne(matchDto.getTeamOne().asEntity())
                                .teamTwo(matchDto.getTeamTwo().asEntity())
                                .scoreTeamOne(matchDto.getScoreTeamOne())
                                .scoreTeamTwo(matchDto.getScoreTeamTwo())
                                .ended(matchDto.isEnded())
                                .build()
                );
            }

            createTournamentMatch(tournamentMatches, 1, null, teams.size());
            List<TournamentMatch> tournamentMatchesInFirstRound = tournamentMatches.stream()
                    .filter(tournamentMatch -> tournamentMatch.getRound() * teams.size() / 2 == 1)
                    .collect(Collectors.toList());

            for (int i = 0; i < tournamentMatchesInFirstRound.size(); i++) {
                tournamentMatchesInFirstRound.get(i).setMatch(matches.get(i));
            }

        } else {
            throw new BadRequestException(teams.size() > 1 ? "Teams size isn't power of 2!" : "Teams size is less than 2");
        }

        return tournamentMatches;
    }

    private Set<Team> getTeams(TournamentDto tournamentDto) {
        Set<Team> teams;
        if (tournamentDto.getTeams().size() > 0) {

            teams = tournamentDto.getTeams()
                    .stream().map(teamDto -> {
                        Set<Player> players = teamDto.getPlayers().stream()
                                .map((playerDto -> Player.builder()
                                        .id(playerDto.getId())
                                        .name(playerDto.getName())
                                        .surname(playerDto.getSurname())
                                        .nickname(playerDto.getNickname())
                                        .build()))
                                .collect(Collectors.toSet());

                        return Team.builder()
                                .id(teamDto.getId())
                                .name(teamDto.getName())
                                .players(players)
                                .build();
                    }).collect(Collectors.toSet());

        } else {
            teams = new HashSet<>();
        }
        return teams;
    }

    private void createTournamentMatch(Set<TournamentMatch> tournamentMatches, double round, Long nextRoundId, int teamSize) {

        TournamentMatch tournamentMatch = tournamentMatchRepository.save(TournamentMatch
                .builder()
                .nextRoundId(nextRoundId)
                .round(round)
                .build());

        tournamentMatches.add(tournamentMatch);

        if (teamSize / 2 * round != 1) {
            createTournamentMatch(tournamentMatches, round / 2, tournamentMatch.getId(), teamSize);
            createTournamentMatch(tournamentMatches, round / 2, tournamentMatch.getId(), teamSize);
        }

    }

    @Override
    public ResponseEntity deleteTournament(Long id) {
        tournamentRepository.delete(id);

        return ResponseEntity.noContent().build();
    }
}
