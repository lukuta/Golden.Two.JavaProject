package com.goldentwo.service.impl;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.exception.BadRequestException;
import com.goldentwo.model.Match;
import com.goldentwo.model.Team;
import com.goldentwo.model.TournamentMatch;
import com.goldentwo.repository.TournamentMatchRepository;
import com.goldentwo.service.MatchService;
import com.goldentwo.service.MatchesGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchesGeneratorServiceImpl implements MatchesGeneratorService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchesGeneratorServiceImpl.class);

    private final TournamentMatchRepository tournamentMatchRepository;
    private final MatchService matchService;

    @Autowired
    public MatchesGeneratorServiceImpl(TournamentMatchRepository tournamentMatchRepository, MatchService matchService) {
        this.tournamentMatchRepository = tournamentMatchRepository;
        this.matchService = matchService;
    }

    @Override
    public Set<TournamentMatch> generateTournamentMatches(Set<Team> teams, MatchGeneratorType type) {
        if (teams.size() < 2 || (teams.size() & (teams.size() - 1)) != 0) {
            throw new BadRequestException(teams.size() > 1 ? "Teams size isn't power of 2!" : "Teams size is less than 2");
        }

        List<Team> teamsToAssign = new ArrayList<>(teams);
        Set<TournamentMatch> tournamentMatchSet;
        switch (type) {
            case RANDOM:
                Collections.shuffle(teamsToAssign);
                break;
            case COMPETITOR_RANK:
                shuffleByCompetitorRank(teamsToAssign);
                break;
            default:
                LOG.warn("Cannot find case for generator type. Used default one - RANDOM");
                Collections.shuffle(teamsToAssign);
                break;
        }
        tournamentMatchSet = createTournamentMatches(teamsToAssign);

        return tournamentMatchSet;
    }

    private void shuffleByCompetitorRank(List<Team> teamsToAssign) {
        teamsToAssign = teamsToAssign.stream()
                .sorted(Comparator.comparing(Team::getRank))
                .collect(Collectors.toList());

        int halfOfTeamListSize = teamsToAssign.size() / 2;
        for (int i = 1, j = halfOfTeamListSize; i < halfOfTeamListSize; i += 2, j += 2) {
            Collections.swap(teamsToAssign, i, j);
        }

    }

    private Set<TournamentMatch> createTournamentMatches(List<Team> teams) {
        Set<TournamentMatch> tournamentMatches = new HashSet<>();

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i += 2) {
            MatchDto matchDto = matchService.saveMatch(MatchDto.builder()
                    .teamOne(teams.get(i).asDto())
                    .teamTwo(teams.get(i + 1).asDto())
                    .build());

            matches.add(
                    Match.builder()
                            .id(matchDto.getId())
                            .teamOne(matchDto.getTeamOne().asEntity())
                            .teamTwo(matchDto.getTeamTwo().asEntity())
                            .scoreTeamOne(matchDto.getScoreTeamOne())
                            .scoreTeamTwo(matchDto.getScoreTeamTwo())
                            .ended(matchDto.isEnded())
                            .build()
            );
        }

        createTournamentMatch(tournamentMatches, 1, null, teams.size());
        List<TournamentMatch> tournamentMatchesInFirstRound = tournamentMatches.stream()
                .filter(tournamentMatch -> tournamentMatch.getRound() * teams.size() / 2 == 1)
                .collect(Collectors.toList());

        for (int i = 0; i < tournamentMatchesInFirstRound.size(); i++) {
            tournamentMatchesInFirstRound.get(i).setMatch(matches.get(i));
        }

        return tournamentMatches;
    }

    private void createTournamentMatch(Set<TournamentMatch> tournamentMatches, double round, Long nextRoundId, int teamSize) {

        TournamentMatch tournamentMatch = tournamentMatchRepository.save(TournamentMatch
                .builder()
                .nextRoundId(nextRoundId)
                .round(round)
                .build());

        tournamentMatches.add(tournamentMatch);

        if (teamSize / 2 * round != 1) {
            createTournamentMatch(tournamentMatches, round / 2, tournamentMatch.getId(), teamSize);
            createTournamentMatch(tournamentMatches, round / 2, tournamentMatch.getId(), teamSize);
        }
    }
}
