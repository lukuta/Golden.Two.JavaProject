package com.goldentwo.service.impl;

import com.goldentwo.dto.*;
import com.goldentwo.exception.BadRequestException;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.Match;
import com.goldentwo.model.Turn;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.TurnRepository;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    public static final int TURN_POINT_VALUE = 1;
    public static final int WINNING_SCORE = 16;

    private MatchRepository matchRepository;

    private TurnRepository turnRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, TurnRepository turnRepository) {
        this.matchRepository = matchRepository;
        this.turnRepository = turnRepository;
    }

    @Override
    public MatchDto findMatchById(Long id) {
        return Optional.ofNullable(matchRepository.findOne(id))
                .orElseThrow(
                        () -> new NotFoundException("Match doesn't exists!"))
                .asDto();
    }

    @Override
    public List<MatchDto> findAllMatches() {
        return matchRepository.findAll().stream()
                .map(Match::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public MatchDto createMatch(MatchDto matchDto) {
        Match match = matchDto.asEntity();

        return matchRepository.save(match).asDto();
    }

    @Override
    public MatchDto updateMatch(MatchDto matchDto) {
        if (matchDto.getId() == null) {
            throw new BadRequestException("Missing id param in request body");
        }

        Match match = matchDto.asEntity();

        return matchRepository.save(match).asDto();
    }

    @Override
    public TurnDto addTurn(Long matchId, TurnDto turnDto) {
        MatchDto matchDto = findMatchById(matchId);

        if (matchDto == null) {
            throw new NotFoundException("Match " + matchId + " not found");
        }

        if (matchDto.isEnded()) {
            throw new BadRequestException("Match was ended");
        }

        Turn turn = turnDto.asEntity();

        Turn savedTurn = turnRepository.save(turn);

        matchDto.getTurns().add(savedTurn.asDto());

        if (matchDto.getTeamOne().getId().equals(savedTurn.getWinner())) {
            matchDto.setScoreTeamOne(matchDto.getScoreTeamOne() + TURN_POINT_VALUE);
        } else {
            matchDto.setScoreTeamTwo(matchDto.getScoreTeamTwo() + TURN_POINT_VALUE);
        }

        if (matchDto.getScoreTeamOne() == WINNING_SCORE || matchDto.getScoreTeamTwo() == WINNING_SCORE) {
            matchDto.setEnded(true);
        }

        updateMatch(matchDto);

        return savedTurn.asDto();
    }

    @Override
    public MatchSummaryDto getMatchSummary(Long matchId) {
        MatchDto matchDto = findMatchById(matchId);

        MatchSummaryDto matchSummaryDto = MatchSummaryDto.builder().matchId(matchId).teamOne(matchDto.getTeamOne())
                .teamTwo(matchDto.getTeamTwo()).scoreTeamOne(matchDto.getScoreTeamOne()).scoreTeamTwo(matchDto.getScoreTeamTwo())
                .ended(matchDto.isEnded()).build();

        Set<MatchPlayerSummaryDto> teamOneStats = matchDto.getTeamOne().getPlayers().stream()
                .map((PlayerDto player) -> MatchPlayerSummaryDto.builder().playerNickname(player.getNickname())
                        .kills(0).deaths(0).build()).collect(Collectors.toSet());

        matchSummaryDto.setTeamOneStatistics(teamOneStats);

        Set<MatchPlayerSummaryDto> teamTwoStats = matchDto.getTeamTwo().getPlayers().stream()
                .map((PlayerDto player) -> MatchPlayerSummaryDto.builder().playerNickname(player.getNickname())
                        .kills(0).deaths(0).build()).collect(Collectors.toSet());

        matchSummaryDto.setTeamTwoStatistics(teamTwoStats);

        for (TurnDto turn : matchDto.getTurns()) {

            countStatsForAllPlayersInSingleTurn(teamOneStats, turn);

            countStatsForAllPlayersInSingleTurn(teamTwoStats, turn);
        }

        return matchSummaryDto;
    }

    private void countStatsForAllPlayersInSingleTurn(Set<MatchPlayerSummaryDto> teamOneStats, TurnDto turn) {
        for (MatchPlayerSummaryDto playerStats : teamOneStats) {
            if (turn.getKills().containsKey(playerStats.getPlayerNickname())) {
                playerStats.setKills(playerStats.getKills() + turn.getKills().get(playerStats.getPlayerNickname()));
            }

            if (turn.getDeaths().contains(playerStats.getPlayerNickname())) {
                playerStats.setDeaths(playerStats.getDeaths() + 1);
            }
        }
    }


    @Override
    public ResponseEntity deleteMatch(Long id) {
        matchRepository.delete(id);

        return ResponseEntity.ok().build();
    }

}
