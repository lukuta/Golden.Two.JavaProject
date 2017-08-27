package com.goldentwo.service.league;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.League;
import com.goldentwo.model.Match;
import com.goldentwo.model.Round;
import com.goldentwo.model.Team;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.service.impl.LeagueServiceImpl;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;

    @InjectMocks
    private LeagueServiceImpl sut;

    private League leagueOne;
    private League leagueTwo;
    private League leagueThree;
    private League leagueFour;

    private LeagueDto leagueOneDto;
    private LeagueDto leagueTwoDto;
    private LeagueDto leagueThreeDto;
    private LeagueDto leagueFourDto;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        Team teamOne = Team.builder()
                .id(1L).name("Virtus.Pro")
                .players(Sets.newHashSet())
                .build();

        Team teamTwo = Team.builder()
                .id(2L).name("Fnatic")
                .players(Sets.newHashSet())
                .build();

        Team teamThree = Team.builder()
                .id(3L).name("Kinguin")
                .players(Sets.newHashSet())
                .build();

        Team teamFour = Team.builder()
                .id(4L).name("NIP")
                .players(Sets.newHashSet())
                .build();

        Match matchOne = Match.builder()
                .id(1L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamOne).teamTwo(teamFour)
                .ended(false).build();

        Match matchTwo = Match.builder()
                .id(2L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamTwo).teamTwo(teamThree)
                .ended(false).build();

        Match matchThree = Match.builder()
                .id(3L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamFour).teamTwo(teamTwo)
                .ended(false).build();

        Match matchFour = Match.builder()
                .id(4L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree).teamTwo(teamOne)
                .ended(false).build();

        Match matchFive = Match.builder()
                .id(5L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree).teamTwo(teamFour)
                .ended(false).build();

        Match matchSix = Match.builder()
                .id(6L).scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamOne).teamTwo(teamTwo)
                .ended(false).build();

        Round roundOne = Round.builder()
                .id(1L).no(1)
                .matches(Sets.newHashSet(matchOne, matchTwo)).build();

        Round roundTwo = Round.builder()
                .id(2L).no(2)
                .matches(Sets.newHashSet(matchThree, matchFour)).build();

        Round roundThree = Round.builder()
                .id(3L).no(3)
                .matches(Sets.newHashSet(matchFive, matchSix)).build();

        leagueOne = League.builder().id(1L)
                .name("ESL")
                .actualRound(1)
                .rounds(Sets.newHashSet())
                .teams(Sets.newHashSet())
                .build();

        leagueOneDto = leagueOne.asDto();

        leagueTwo = League.builder().id(2L)
                .name("FaceIt")
                .actualRound(0)
                .rounds(Sets.newHashSet(roundOne, roundTwo, roundThree))
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();

        leagueTwoDto = leagueTwo.asDto();

        leagueThree = League.builder()
                .id(null)
                .name("FaceIt")
                .actualRound(0)
                .rounds(Sets.newHashSet())
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();

        leagueThreeDto = leagueThree.asDto();

        leagueFour = leagueThree;
        leagueFour.setRounds(leagueTwo.getRounds());
        leagueFourDto = leagueFour.asDto();
    }

    @Test
    public void findAllLeaguesTest() {

        Mockito
                .when(leagueRepository.findAll())
                .thenReturn(Arrays.asList(leagueOne, leagueTwo));

        Set<LeagueDto> listFromSut = sut.findAllLeagues();

        assertThat(listFromSut)
                .isNotNull()
                .isEqualTo(Sets.newHashSet(leagueOneDto, leagueTwoDto));

    }

    @Test
    public void findLeagueByIdWhenExist() {
        Long leagueId = 1L;

        Mockito
                .when(leagueRepository.findOne(leagueId))
                .thenReturn(leagueOne);

        LeagueDto leagueFromSut = sut.findLeagueById(leagueId);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueOneDto);
    }

    @Test(expected = NotFoundException.class)
    public void findLeagueByIdWhenNotExist() {
        Long leagueId = 1L;

        Mockito
                .when(leagueRepository.findOne(leagueId))
                .thenReturn(null);

        sut.findLeagueById(leagueId);
    }

    @Test
    public void findLeagueByNameWhenExist() {
        String leagueName = "ESL";

        Mockito
                .when(leagueRepository.findByName(leagueName))
                .thenReturn(Optional.ofNullable(leagueOne));

        LeagueDto leagueFromSut = sut.findLeagueByName(leagueName);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueOneDto);
    }

    @Test(expected = NotFoundException.class)
    public void findLeagueByNameWhenNotExist() {
        String leagueName = "ELeague";

        Mockito
                .when(leagueRepository.findByName(leagueName))
                .thenReturn(Optional.empty());

        sut.findLeagueByName(leagueName);
    }

    @Test
    public void createNewLeagueTest() {
        Mockito
                .when(leagueRepository.save(leagueFour))
                .thenReturn(leagueTwo);

        System.out.println(leagueFour);

        LeagueDto leagueFromSut = sut.saveLeague(leagueThreeDto);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueTwoDto);
    }
}
