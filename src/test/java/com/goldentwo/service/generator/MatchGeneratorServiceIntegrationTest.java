package com.goldentwo.service.generator;

import com.goldentwo.Janitor;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.model.Team;
import com.goldentwo.model.TournamentMatch;
import com.goldentwo.repository.*;
import com.goldentwo.service.MatchesGeneratorService;
import com.goldentwo.service.PlayerService;
import com.google.common.collect.Sets;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchGeneratorServiceIntegrationTest {

    public static final int RANK_POSITION_DIFFERENCE = 4;
    public static final int BASKET_SIZE = 4;

    @Autowired
    private MatchesGeneratorService matchesGeneratorService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private Janitor janitor;

    @Before
    public void initialize() {
        tearDown();
    }

    @After
    public void tearDown() {
        janitor.clearRepos();
    }

    @Test
    public void matchTeamsRankShouldBeVaryAboutGivenRankPositions() {
        initPlayers(8);
        List<Team> teamList = teamRepository.findAll();

        Set<TournamentMatch> tournamentMatches = matchesGeneratorService.generateTournamentMatches(
                Sets.newHashSet(teamList),
                MatchesGeneratorService.MatchGeneratorType.COMPETITOR_RANK
        );

        Condition<TournamentMatch> competitorRankMatchCondition =
                ConditionCreator.createCompetitorRankMatchCondition(RANK_POSITION_DIFFERENCE);

        tournamentMatches.forEach(tournamentMatch -> {
            if (tournamentMatch.getMatch() != null) {
                assertThat(tournamentMatch)
                        .is(competitorRankMatchCondition);
            }
        });
    }

    @Test
    public void ensureThatAllMatchesWereDrawnFromSeparateBaskets() {
        initPlayers(16);
        List<Team> teamList = teamRepository.findAll();

        Set<TournamentMatch> tournamentMatches = matchesGeneratorService.generateTournamentMatches(
                Sets.newHashSet(teamList),
                MatchesGeneratorService.MatchGeneratorType.BASKETS
        );

        Condition<TournamentMatch> basketMatchCondition =
                ConditionCreator.createBasketMatchCondition(teamList.size());

        tournamentMatches.forEach(tournamentMatch -> {
            if (tournamentMatch.getMatch() != null) {
                assertThat(tournamentMatch)
                        .is(basketMatchCondition);
            }
        });
    }

    private void initPlayers(int playersAmount) {
        for (int i = 1; i <= playersAmount; i++) {
            playerService.savePlayer(
                    PlayerDto.builder()
                            .nickname("nick" + i)
                            .name("name" + i)
                            .surname("surname" + i)
                            .rankPoints(i)
                            .build()
            );
        }
    }

    static class ConditionCreator {
        static Condition<TournamentMatch> createBasketMatchCondition(int teamAmount) {
            return new Condition<TournamentMatch>() {
                @Override
                public boolean matches(TournamentMatch tournamentMatch) {
                    int rankDifferenceBetweenOneBasketTeams = teamAmount / BASKET_SIZE;

                    long teamOneId = tournamentMatch.getMatch().getTeamOne().getId();
                    long teamTwoId = tournamentMatch.getMatch().getTeamTwo().getId();

                    return Math.abs(teamOneId - teamTwoId) % rankDifferenceBetweenOneBasketTeams != 0;
                }
            };
        }

        static Condition<TournamentMatch> createCompetitorRankMatchCondition(int idDifference) {
            return new Condition<TournamentMatch>() {
                @Override
                public boolean matches(TournamentMatch tournamentMatch) {
                    long teamOneId = tournamentMatch.getMatch().getTeamOne().getId();
                    long teamTwoId = tournamentMatch.getMatch().getTeamTwo().getId();

                    return Math.abs(teamOneId - teamTwoId) == idDifference;
                }
            };
        }
    }
}
