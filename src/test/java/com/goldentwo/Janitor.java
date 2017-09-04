package com.goldentwo;

import com.goldentwo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Janitor {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentMatchRepository tournamentMatchRepository;

    public void clearRepos() {
        tournamentRepository.deleteAll();
        tournamentMatchRepository.deleteAll();
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }
}
