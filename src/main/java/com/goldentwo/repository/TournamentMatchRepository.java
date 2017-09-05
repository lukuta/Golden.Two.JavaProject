package com.goldentwo.repository;


import com.goldentwo.model.TournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentMatchRepository extends JpaRepository<TournamentMatch, Long> {
}
