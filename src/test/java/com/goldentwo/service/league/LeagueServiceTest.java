package com.goldentwo.service.league;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.League;
import com.goldentwo.model.Round;
import com.goldentwo.model.Team;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.repository.RoundRepository;
import com.goldentwo.service.MatchService;
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

    @Mock
    private MatchService matchService;

    @Mock
    private RoundRepository roundRepository;

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

    private MatchDto matchOne;
    private MatchDto savedMatchOne;
    private MatchDto matchTwo;
    private MatchDto savedMatchTwo;
    private MatchDto matchThree;
    private MatchDto savedMatchThree;
    private MatchDto matchFour;
    private MatchDto savedMatchFour;
    private MatchDto matchFive;
    private MatchDto savedMatchFive;
    private MatchDto matchSix;
    private MatchDto savedMatchSix;

    private Round roundOne;
    private Round savedRoundOne;
    private Round roundTwo;
    private Round savedRoundTwo;
    private Round roundThree;
    private Round savedRoundThree;

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

        matchOne = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamFour.asDto()).teamTwo(teamOne.asDto())
                .ended(false).build();

        savedMatchOne = MatchDto.builder()
                .id(1L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamFour.asDto()).teamTwo(teamOne.asDto())
                .ended(false).build();

        matchTwo = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamTwo.asDto()).teamTwo(teamThree.asDto())
                .ended(false).build();

        savedMatchTwo = MatchDto.builder()
                .id(2L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamTwo.asDto()).teamTwo(teamThree.asDto())
                .ended(false).build();

        matchThree = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree.asDto()).teamTwo(teamFour.asDto())
                .ended(false).build();

        savedMatchThree = MatchDto.builder()
                .id(3L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree.asDto()).teamTwo(teamFour.asDto())
                .ended(false).build();


        matchFour = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamOne.asDto()).teamTwo(teamTwo.asDto())
                .ended(false).build();

        savedMatchFour = MatchDto.builder()
                .id(4L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamOne.asDto()).teamTwo(teamTwo.asDto())
                .ended(false).build();

        matchFive = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamFour.asDto()).teamTwo(teamTwo.asDto())
                .ended(false).build();

        savedMatchFive = MatchDto.builder()
                .id(5L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamFour.asDto()).teamTwo(teamTwo.asDto())
                .ended(false).build();

        matchSix = MatchDto.builder()
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree.asDto()).teamTwo(teamOne.asDto())
                .ended(false).build();

        savedMatchSix = MatchDto.builder()
                .id(6L)
                .scoreTeamOne(0).scoreTeamTwo(0)
                .teamOne(teamThree.asDto()).teamTwo(teamOne.asDto())
                .ended(false).build();

        roundOne = Round.builder()
                .no(1)
                .matches(Sets.newHashSet(matchOne.asEntity(), matchTwo.asEntity())).build();

        savedRoundOne = Round.builder()
                .no(1).id(1L)
                .matches(Sets.newHashSet(matchOne.asEntity(), matchTwo.asEntity())).build();

        roundTwo = Round.builder()
                .no(2)
                .matches(Sets.newHashSet(matchThree.asEntity(), matchFour.asEntity())).build();

        savedRoundTwo = Round.builder()
                .no(2).id(2L)
                .matches(Sets.newHashSet(matchThree.asEntity(), matchFour.asEntity())).build();

        roundThree = Round.builder()
                .no(3)
                .matches(Sets.newHashSet(matchFive.asEntity(), matchSix.asEntity())).build();

        savedRoundThree = Round.builder()
                .no(3).id(3L)
                .matches(Sets.newHashSet(matchFive.asEntity(), matchSix.asEntity())).build();

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
                .rounds(Sets.newHashSet(savedRoundOne, savedRoundTwo, savedRoundThree))
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();

        leagueTwoDto = leagueTwo.asDto();

        leagueThree = League.builder()
                .name("FaceIt")
                .actualRound(0)
                .rounds(Sets.newHashSet())
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();

        leagueThreeDto = leagueThree.asDto();

        leagueFour = League.builder()
                .name("FaceIt")
                .actualRound(0)
                .rounds(Sets.newHashSet(roundOne, roundTwo, roundThree))
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();
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
                .when(matchService.saveMatch(matchOne))
                .thenReturn(savedMatchOne);
        System.out.println(matchOne);

        Mockito
                .when(matchService.saveMatch(matchTwo))
                .thenReturn(savedMatchTwo);

        Mockito
                .when(matchService.saveMatch(matchThree))
                .thenReturn(savedMatchThree);

        Mockito
                .when(matchService.saveMatch(matchFour))
                .thenReturn(savedMatchFour);

        Mockito
                .when(matchService.saveMatch(matchFive))
                .thenReturn(savedMatchFive);

        Mockito
                .when(matchService.saveMatch(matchSix))
                .thenReturn(savedMatchSix);

        Mockito
                .when(roundRepository.save(roundOne))
                .thenReturn(savedRoundOne);

        Mockito
                .when(roundRepository.save(roundTwo))
                .thenReturn(savedRoundTwo);

        Mockito
                .when(roundRepository.save(roundThree))
                .thenReturn(savedRoundThree);

        Mockito
                .when(leagueRepository.save(leagueFour))
                .thenReturn(leagueTwo);

        LeagueDto leagueFromSut = sut.saveLeague(leagueThreeDto);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueTwoDto);
    }
}
