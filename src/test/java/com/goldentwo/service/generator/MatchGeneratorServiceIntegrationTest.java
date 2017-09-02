package com.goldentwo.service.generator;

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

    @Autowired
    private MatchesGeneratorService matchesGeneratorService;

    @Autowired
    private TournamentMatchRepository tournamentMatchRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;

    @Before
    public void initialize() {
        matchRepository.deleteAll();
        tournamentMatchRepository.deleteAll();
        tournamentRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @After
    public void tearDown() {
        matchRepository.deleteAll();
        tournamentMatchRepository.deleteAll();
        tournamentRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void teamsMatchRankShouldBeVaryAbout4Ranks() {
        initPlayers();
        List<Team> teamList = teamRepository.findAll();

        Set<TournamentMatch> tournamentMatches = matchesGeneratorService.generateTournamentMatches(
                Sets.newHashSet(teamList),
                MatchesGeneratorService.MatchGeneratorType.COMPETITOR_RANK
        );

        Condition<TournamentMatch> tournamentMatchCondition = createTournamentMatchCondition(4);

        tournamentMatches.forEach(tournamentMatch -> {
            if (tournamentMatch.getMatch() != null) {
                assertThat(tournamentMatch)
                        .is(tournamentMatchCondition);
            }
        });

    }

    private Condition<TournamentMatch> createTournamentMatchCondition(int rankDifference) {
        return new Condition<TournamentMatch>() {
            @Override
            public boolean matches(TournamentMatch tournamentMatch) {
                int teamOneRank = tournamentMatch.getMatch().getTeamOne().getRank();
                int teamTwoRank = tournamentMatch.getMatch().getTeamTwo().getRank();

                return Math.abs(teamOneRank - teamTwoRank) == rankDifference;
            }
        };
    }

    private void initPlayers() {
        for (int i = 1; i < 9; i++) {
            playerService.savePlayer(
                    PlayerDto.builder()
                            .nickname("nick" + i)
                            .name("name" + i)
                            .surname("surname" + i)
                            .rank(i)
                            .build()
            );
        }
    }
}
