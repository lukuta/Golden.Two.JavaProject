package com.goldentwo.repository;

import com.goldentwo.model.Match;
import com.goldentwo.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<List<Match>> findMatchesByTeamOneOrTeamTwo(Team teamOne, Team teamTwo);
}
