package com.goldentwo.service.impl;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TurnDto;
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

import java.util.List;
import java.util.Optional;
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

        return matchRepository.saveAndFlush(match).asDto();
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
            matchDto.setScoreTeamOne(matchDto.getScoreTeamTwo() + TURN_POINT_VALUE);
        }

        if (matchDto.getScoreTeamOne() == WINNING_SCORE || matchDto.getScoreTeamTwo() == WINNING_SCORE) {
            matchDto.setEnded(true);
        }

        updateMatch(matchDto);

        return savedTurn.asDto();
    }


    @Override
    public ResponseEntity deleteMatch(Long id) {
        matchRepository.delete(id);

        return ResponseEntity.ok().build();
    }

}
