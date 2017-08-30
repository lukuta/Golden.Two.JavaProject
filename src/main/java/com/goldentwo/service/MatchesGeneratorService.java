package com.goldentwo.service;

import com.goldentwo.model.Team;
import com.goldentwo.model.TournamentMatch;

import java.util.Set;

public interface MatchesGeneratorService {
    Set<TournamentMatch> generateTournamentMatches(Set<Team> teams, MatchGeneratorType type);

    enum MatchGeneratorType {
        RANDOM, COMPETITOR_RANK
    }
}
