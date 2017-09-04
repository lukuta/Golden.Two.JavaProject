package com.goldentwo.repository;

import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
    Optional<List<Team>> findTeamsByPlayers(Player players);
}
