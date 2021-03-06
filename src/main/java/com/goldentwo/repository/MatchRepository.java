package com.goldentwo.repository;

import com.goldentwo.model.Match;
import com.goldentwo.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {
    Optional<List<Match>> findMatchesByTeamOneOrTeamTwo(Team teamOne, Team teamTwo);
    Optional<List<Match>> findMatchesByTeamOneOrTeamTwoAndEnded(Team teamOne, Team teamTwo, boolean ended);
}
