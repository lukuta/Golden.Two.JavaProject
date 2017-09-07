package com.goldentwo.service.impl;

import com.goldentwo.dto.BetDto;
import com.goldentwo.model.Match;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import com.goldentwo.repository.BettingRepository;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class BetServiceImpl implements BetService {

    private final BettingRepository bettingRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public BetServiceImpl(BettingRepository bettingRepository, MatchRepository matchRepository) {
        this.bettingRepository = bettingRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Bet> getOwnBets(Principal principal) {
        String principalName = principal.getName();
        return bettingRepository.findAllByTyper(principalName);
    }

    @Override
    public Bet createBet(Principal principal, BetDto betDto) {
        Match match = matchRepository.findOne(betDto.getMatchId());
        String principalName = principal.getName();
        Bet bet = Bet.builder()
                .betStatus(BetStatus.OPEN)
                .match(match)
                .typer(principalName)
                .betType(betDto.getBetType())
                .build();

        return bettingRepository.saveAndFlush(bet);
    }
}
