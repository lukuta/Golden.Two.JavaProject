package com.goldentwo.repository;

import com.goldentwo.model.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BettingRepository extends JpaRepository<Bet, Long> {

    @Query("select t from Bet t where t.typer = ?1")
    List<Bet> findAllByTyper(String typer);
}
