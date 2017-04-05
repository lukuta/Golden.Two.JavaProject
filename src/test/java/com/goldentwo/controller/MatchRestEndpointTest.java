package com.goldentwo.controller;

import com.goldentwo.model.Match;
import com.goldentwo.service.MatchService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
public class MatchRestEndpointTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchRestEndpoint sut;

    private Match matchOne;
    private Match matchTwo;

    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);

        matchOne = Match.builder().id(1L).build();
        matchTwo = Match.builder().id(2L).build();
    }

    @Test
    public void findAllMatchesTest(){

        List<Match> matches = Arrays.asList(matchOne, matchTwo);

        Mockito
                .when(matchService.findAllMatches())
                .thenReturn(matches);

        List<Match> matchesFromSut = sut.findAllMatches();

        Assertions.assertThat(matchesFromSut)
                .isNotNull()
                .isEqualTo(matches);
    }

    @Test
    public void findMatchByIdTest(){

        Long matchId = matchOne.getId();

        Mockito
                .when(matchService.findMatchById(matchId))
                .thenReturn(matchOne);

        Match matchFromSut = sut.findMatchById(matchId);

        Assertions.assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    @Test
    public void saveMatchTest(){

        Mockito
                .when(matchService.saveMatch(matchOne))
                .thenReturn(matchOne);

        Match savedMatchFromSut = sut.saveMatch(matchOne);

        Assertions.assertThat(savedMatchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    //TODO: deleteMatchTest() ??



}
