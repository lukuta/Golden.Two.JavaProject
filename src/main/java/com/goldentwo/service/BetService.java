package com.goldentwo.service;

import com.goldentwo.dto.BetDto;
import com.goldentwo.model.bet.Bet;

import java.security.Principal;
import java.util.List;

public interface BetService {
    List<Bet> getOwnBets(Principal principal);
    Bet createBet(Principal principal, BetDto betDto);
}
