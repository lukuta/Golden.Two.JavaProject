package com.goldentwo.controller;

import com.goldentwo.dto.BetDto;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.service.BetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bets")
public class BetRestController {

    private final BetService betService;

    @Autowired
    public BetRestController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public Bet createBet(Principal principal,
                         @RequestBody BetDto betDto) {

        if (principal == null) {
            throw new UnauthorizedUserException("Authentication is needed");
        }

        return betService.createBet(principal, betDto);
    }

    @GetMapping("/my")
    public List<Bet> getOwnBets(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedUserException("Authentication is needed");
        }
        return betService.getOwnBets(principal);
    }
}
