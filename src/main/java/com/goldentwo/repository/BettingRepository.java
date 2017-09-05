package com.goldentwo.repository;

import com.goldentwo.model.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingRepository extends JpaRepository<Bet, Long> {
}
