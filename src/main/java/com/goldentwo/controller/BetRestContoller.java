package com.goldentwo.controller;

import com.goldentwo.dto.BetDto;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bets")
public class BetRestContoller {

    private final BetService betService;

    @Autowired
    public BetRestContoller(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public Bet createBet(Principal principal,
                         @RequestBody BetDto betDto) {

        if (principal.getName() == null) {
            throw new UnauthorizedUserException("Authentication is needed");
        }

        return betService.createBet(principal, betDto);
    }

    @PostMapping("/my")
    public List<Bet> getOwnBets(Principal principal) {
        return betService.getOwnBets(principal);
    }
}
