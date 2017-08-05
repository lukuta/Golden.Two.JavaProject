package com.goldentwo.controller.match;

import com.goldentwo.controller.MatchRestEndpoint;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.service.MatchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class MatchRestEndpointTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchRestEndpoint sut;

    private MatchDto matchOne;
    private MatchDto matchTwo;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        matchOne = MatchDto.builder().id(1L).build();
        matchTwo = MatchDto.builder().id(2L).build();
    }

    @Test
    public void findAllMatchesTest() {
        List<MatchDto> matches = Arrays.asList(matchOne, matchTwo);

        Mockito
                .when(matchService.findAllMatches())
                .thenReturn(matches);

        List<MatchDto> matchesFromSut = sut.findAllMatches();

        assertThat(matchesFromSut)
                .isNotNull()
                .isEqualTo(matches);
    }

    @Test
    public void findMatchByIdTest() {
        Long matchId = matchOne.getId();

        Mockito
                .when(matchService.findMatchById(matchId))
                .thenReturn(matchOne);

        MatchDto matchFromSut = sut.findMatchById(matchId);

        assertThat(matchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    @Test
    public void saveMatchTest() {
        Mockito
                .when(matchService.saveMatch(matchOne))
                .thenReturn(matchOne);

        MatchDto savedMatchFromSut = sut.saveMatch(matchOne);

        assertThat(savedMatchFromSut)
                .isNotNull()
                .isEqualTo(matchOne);
    }

    @Test
    public void deleteMatchTest() {
        Long id = 1L;
        ResponseEntity expectedResponse = ResponseEntity.ok().build();

        Mockito
                .when(matchService.deleteMatch(id))
                .thenReturn(expectedResponse);

        ResponseEntity responseFromSut = sut.deleteMatch(id);

        assertThat(responseFromSut)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }


}