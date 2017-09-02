package com.goldentwo.service.generator;


import com.goldentwo.model.Match;
import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import com.goldentwo.model.TournamentMatch;
import com.goldentwo.repository.TournamentMatchRepository;
import com.goldentwo.service.MatchService;
import com.goldentwo.service.impl.MatchesGeneratorServiceImpl;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static com.goldentwo.service.MatchesGeneratorService.MatchGeneratorType;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class MatchGeneratorServiceImplTest {

    @Mock
    private TournamentMatchRepository tournamentMatchRepository;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchesGeneratorServiceImpl sut;

    private Team[] teams;
    private Set<Team> teamSet;
    private Set<TournamentMatch> tournamentMatchSet;
    private Set<TournamentMatch> tournamentLadderMatchSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        teams = new Team[]{
                Team.builder().id(1L).name("A").rankPoints(10).players(
                        Sets.newHashSet(Player.builder().nickname("AA").rankPoints(10).build())
                ).build(),
                Team.builder().id(2L).name("B").rankPoints(9).players(
                        Sets.newHashSet(Player.builder().nickname("BB").rankPoints(9).build())
                ).build(),
                Team.builder().id(3L).name("C").rankPoints(8).players(
                        Sets.newHashSet(Player.builder().nickname("CC").rankPoints(8).build())
                ).build(),
                Team.builder().id(4L).name("D").rankPoints(7).players(
                        Sets.newHashSet(Player.builder().nickname("DD").rankPoints(7).build())
                ).build(),
                Team.builder().id(5L).name("E").rankPoints(6).players(
                        Sets.newHashSet(Player.builder().nickname("EE").rankPoints(6).build())
                ).build(),
                Team.builder().id(6L).name("F").rankPoints(5).players(
                        Sets.newHashSet(Player.builder().nickname("FF").rankPoints(5).build())
                ).build(),
                Team.builder().id(7L).name("G").rankPoints(4).players(
                        Sets.newHashSet(Player.builder().nickname("GG").rankPoints(4).build())
                ).build(),
                Team.builder().id(8L).name("H").rankPoints(3).players(
                        Sets.newHashSet(Player.builder().nickname("HH").rankPoints(3).build())
                ).build(),
        };
        teamSet = Sets.newHashSet(
                teams
        );
        tournamentMatchSet = Sets.newHashSet(
                TournamentMatch.builder().id(1L).round(0.25).match(
                        Match.builder().teamOne(teams[0]).teamTwo(teams[4]).build()
                ).build(),
                TournamentMatch.builder().id(2L).round(0.25).match(
                        Match.builder().teamOne(teams[1]).teamTwo(teams[5]).build()
                ).build(),
                TournamentMatch.builder().id(3L).round(0.25).match(
                        Match.builder().teamOne(teams[2]).teamTwo(teams[6]).build()
                ).build(),
                TournamentMatch.builder().id(4L).round(0.25).match(
                        Match.builder().teamOne(teams[3]).teamTwo(teams[7]).build()
                ).build()
        );
        tournamentLadderMatchSet = Sets.newHashSet(
                TournamentMatch.builder().id(1L).round(1).nextRoundId(null).build(),
                TournamentMatch.builder().id(2L).round(0.5).nextRoundId(1L).build(),
                TournamentMatch.builder().id(3L).round(0.5).nextRoundId(1L).build(),
                TournamentMatch.builder().id(4L).round(0.25).nextRoundId(2L).build(),
                TournamentMatch.builder().id(5L).round(0.25).nextRoundId(2L).build(),
                TournamentMatch.builder().id(6L).round(0.25).nextRoundId(3L).build(),
                TournamentMatch.builder().id(7L).round(0.25).nextRoundId(3L).build()
        );
    }

    @Test
    public void competitorRankGeneratorTest() {
        //TODO cannot properly mock tournamentMatchRepository behavior
//        tournamentMatchSet.forEach(
//                tournamentMatch -> Mockito
//                        .when(matchService.saveMatch(tournamentMatch.getMatch().asDto()))
//                        .thenReturn(tournamentMatch.getMatch().asDto())
//        );
//
//        tournamentLadderMatchSet.forEach(
//                tournamentMatch -> {
//                    TournamentMatch tournamentMatchWithoutId =
//                            TournamentMatch.builder()
//                                    .nextRoundId(tournamentMatch.getNextRoundId())
//                                    .round(tournamentMatch.getRound())
//                                    .build();
//                    Mockito
//                            .when(tournamentMatchRepository.save(tournamentMatchWithoutId))
//                            .thenReturn(tournamentMatch);
//                }
//        );
//
//        Set<TournamentMatch> tournamentMatchesFromSut =
//                sut.generateTournamentMatches(teamSet, MatchGeneratorType.COMPETITOR_RANK);
//
//        assertThat(tournamentMatchesFromSut)
//                .isEqualTo(tournamentMatchSet);
    }

}
