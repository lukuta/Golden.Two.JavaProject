package com.goldentwo.service.cron;

import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import com.goldentwo.repository.BettingRepository;
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

    @Autowired
    public BetCollectorService(BettingRepository bettingRepository) {
        this.bettingRepository = bettingRepository;
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
        //TODO add points
    }
}
