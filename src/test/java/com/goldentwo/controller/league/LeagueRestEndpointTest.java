package com.goldentwo.controller.league;

import com.goldentwo.controller.LeagueRestEndpoint;
import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.League;
import com.goldentwo.model.Round;
import com.goldentwo.model.Team;
import com.goldentwo.service.LeagueService;
import com.goldentwo.service.impl.LeagueServiceImpl;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
public class LeagueRestEndpointTest {

    @Mock
    private LeagueService leagueService;

    @InjectMocks
    private LeagueRestEndpoint sut;

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

    private MockMvc mockMvc;

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
                .matches(Sets.newHashSet(savedMatchOne.asEntity(), savedMatchTwo.asEntity())).build();

        savedRoundOne = Round.builder()
                .no(1).id(1L)
                .matches(Sets.newHashSet(savedMatchOne.asEntity(), savedMatchTwo.asEntity())).build();

        roundTwo = Round.builder()
                .no(2)
                .matches(Sets.newHashSet(savedMatchThree.asEntity(), savedMatchFour.asEntity())).build();

        savedRoundTwo = Round.builder()
                .no(2).id(2L)
                .matches(Sets.newHashSet(savedMatchThree.asEntity(), savedMatchFour.asEntity())).build();

        roundThree = Round.builder()
                .no(3)
                .matches(Sets.newHashSet(savedMatchFive.asEntity(), savedMatchSix.asEntity())).build();

        savedRoundThree = Round.builder()
                .no(3).id(3L)
                .matches(Sets.newHashSet(savedMatchFive.asEntity(), savedMatchSix.asEntity())).build();

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
                .rounds(Sets.newHashSet(savedRoundOne, savedRoundTwo, savedRoundThree))
                .teams(Sets.newHashSet(teamOne, teamTwo, teamThree, teamFour))
                .build();
        leagueFourDto = leagueFour.asDto();

        mockMvc = MockMvcBuilders.standaloneSetup(new NotFoundException(), leagueService).build();
    }

    @Test
    public void findAllLeagues() {
        Set<LeagueDto> leagues = Sets.newHashSet(leagueOneDto, leagueTwoDto);

        Mockito
                .when(leagueService.findAllLeagues())
                .thenReturn(leagues);

        Set<LeagueDto> listFromSut = sut.findAllLeagues();

        assertThat(listFromSut)
                .isNotNull()
                .isEqualTo(leagues);
    }

    @Test
    public void findLeagueByIdWhenExist() {
        Long leagueId = 1L;

        Mockito
                .when(leagueService.findLeagueById(leagueId))
                .thenReturn(leagueOneDto);

        LeagueDto leagueFromSut = sut.findLeagueById(leagueId);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueOneDto);
    }

    @Test
    public void findLeagueByIdWhenNotExist() throws Exception {
        Long leagueId = 10L;

        Mockito
                .when(leagueService.findLeagueById(leagueId))
                .thenThrow(new NotFoundException("League " + leagueId + " doesn't exist"));

        mockMvc.perform(get("/leagues/{id}", leagueId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findLeagueByNameWhenExist() {
        String leagueName = "ESL";

        Mockito
                .when(leagueService.findLeagueByName(leagueName))
                .thenReturn(leagueOneDto);

        LeagueDto leagueFromSut = sut.findLeagueByName(leagueName);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueOneDto);

        assertThat(leagueFromSut.getName())
                .isEqualTo(leagueName);
    }

    @Test
    public void createLeagueTest() {
        Mockito
                .when(leagueService.saveLeague(leagueThreeDto))
                .thenReturn(leagueTwoDto);

        LeagueDto leagueFromSut = sut.saveLeague(leagueThreeDto);

        assertThat(leagueFromSut)
                .isNotNull()
                .isEqualTo(leagueTwoDto);
    }

    @Test
    public void deleteLeagueTest() {
        Long leagueId = 1L;

        Mockito
                .when(leagueService.deleteLeague(leagueId))
                .thenReturn(ResponseEntity.noContent().build());

        ResponseEntity entity = sut.deleteLeague(leagueId);

        assertThat(entity.getStatusCode().value())
                .isEqualTo(204);
    }

}
