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
    private static final int BASKET_SIZE = 4;

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
                teamsToAssign = shuffleByCompetitorRank(teamsToAssign);
                break;
            case BASKETS:
                teamsToAssign = shuffleByBaskets(teamsToAssign);
                break;
            default:
                LOG.warn("Cannot find case for generator type. Used default one - RANDOM");
                Collections.shuffle(teamsToAssign);
                break;
        }
        tournamentMatchSet = createTournamentMatches(teamsToAssign);

        return tournamentMatchSet;
    }

    private List<Team> shuffleByCompetitorRank(List<Team> teamsToAssign) {
        List<Team> teamList = teamsToAssign.stream()
                .sorted(Comparator.comparing(Team::getRankPoints).reversed())
                .collect(Collectors.toList());

        int teamListHalfSize = teamsToAssign.size() / 2;
        for (int i = 1, j = teamListHalfSize; i < teamListHalfSize; i += 2, j += 2) {
            Collections.swap(teamList, i, j);
        }

        return teamList;
    }

    private List<Team> shuffleByBaskets(List<Team> teamsToAssign) {
        checkTeamsAmount(teamsToAssign);

        List<Team> teamList = teamsToAssign.stream()
                .sorted(Comparator.comparing(Team::getRankPoints).reversed())
                .collect(Collectors.toList());

        List<List<Team>> teamBaskets = new ArrayList<>();
        placeTeamsIntoBaskets(teamList, teamBaskets);

        return drawTeams(teamBaskets);
    }

    private List<Team> drawTeams(List<List<Team>> teamBaskets) {
        List<Team> drawnTeams = new ArrayList<>();
        teamBaskets.forEach(Collections::shuffle);
        for (int i = 0; i < BASKET_SIZE; i++) {
            for (List<Team> teamBasket : teamBaskets) {
                drawnTeams.add(teamBasket.get(i));
            }
        }

        return drawnTeams;
    }

    private void placeTeamsIntoBaskets(List<Team> teamList, List<List<Team>> teamBaskets) {
        int basketsAmount = teamList.size() / BASKET_SIZE;

        for (int i = 0; i < BASKET_SIZE; i++) {
            for (int basketIndex = 0; basketIndex < basketsAmount; basketIndex++) {
                int teamIndex = i * basketsAmount + basketIndex;
                teamBaskets.get(basketIndex).add(teamList.get(teamIndex));
            }
        }
    }

    private void checkTeamsAmount(List<Team> teamsToAssign) {
        int teamsAmount = teamsToAssign.size();

        if (teamsAmount < 8) {
            throw new BadRequestException("Teams size in basket type must be greater than 7");
        } else if (teamsAmount > 32) {
            throw new BadRequestException("Team size in basket type must by lesser than 32");
        } else if (teamsAmount % 4 != 0) {
            throw new BadRequestException("Each basket must contain exactly 4 teams");
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
