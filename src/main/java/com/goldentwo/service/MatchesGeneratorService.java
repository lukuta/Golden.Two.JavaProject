package com.goldentwo.service;

import com.goldentwo.model.Team;
import com.goldentwo.model.TournamentMatch;

import java.util.Set;

public interface MatchesGeneratorService {
    Set<TournamentMatch> generateRandomMatches(Set<Team> teams);

    Set<TournamentMatch> generateMatchesByCompetitorRank(Set<Team> teams);
}
