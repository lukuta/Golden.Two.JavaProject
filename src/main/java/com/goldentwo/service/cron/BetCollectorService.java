package com.goldentwo.service.cron;

import com.goldentwo.model.Match;
import com.goldentwo.model.Typer;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import com.goldentwo.repository.BettingRepository;
import com.goldentwo.repository.TyperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BetCollectorService {
    private static final String BET_COLLECTOR_CRON = "0 0/30 * * * *";

    private final BettingRepository bettingRepository;
    private final TyperRepository typerRepository;

    @Autowired
    public BetCollectorService(BettingRepository bettingRepository, TyperRepository typerRepository) {
        this.bettingRepository = bettingRepository;
        this.typerRepository = typerRepository;
    }

    @Scheduled(cron = BET_COLLECTOR_CRON)
    private void finalizeBets() {
        List<Bet> allOpenBets = bettingRepository.findAllByBetStatus(BetStatus.OPEN);
        allOpenBets.forEach(
                bet -> {
                    if (bet.getMatch().isEnded()) {
                        finalizeBet(bet);
                    }
                }
        );
        log.info("Finalized bets");
    }

    private void finalizeBet(Bet bet) {
        bet.setBetStatus(BetStatus.CLOSED);
        Match match = bet.getMatch();
        switch (bet.getBetType()) {
            case ONE:
                if (match.getScoreTeamOne() > match.getScoreTeamTwo()) {
                    addPointForTyper(bet.getTyper());
                }
                break;
            case TWO:
                if (match.getScoreTeamOne() < match.getScoreTeamTwo()) {
                    addPointForTyper(bet.getTyper());
                }
                break;
            case ZERO:
                if (match.getScoreTeamOne() == match.getScoreTeamTwo()) {
                    addPointForTyper(bet.getTyper());
                }
                break;
        }
    }

    private void addPointForTyper(String typerName) {
        Typer typer = typerRepository.findByTyper(typerName)
                .orElse(Typer.builder().points(0).typer(typerName).build());

        typer.setPoints(typer.getPoints() + 1);

        typerRepository.saveAndFlush(typer);
    }
}
