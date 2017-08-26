package com.goldentwo.service.league;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.exception.NotFoundException;
import com.goldentwo.model.League;
import com.goldentwo.repository.LeagueRepository;
import com.goldentwo.service.impl.LeagueServiceImpl;
import com.google.common.collect.Sets;
import org.junit.Assert;
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
import static org.mockito.Matchers.any;

@ActiveProfiles("test")
public class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;

    @InjectMocks
    private LeagueServiceImpl sut;

    private League leagueOne;
    private League leagueTwo;

    private LeagueDto leagueOneDto;
    private LeagueDto leagueTwoDto;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);

        leagueOne = League.builder().id(1L)
                .name("ESL")
                .actualRound(1)
                .rounds(Sets.newHashSet())
                .teams(Sets.newHashSet())
                .build();

        leagueOneDto = leagueOne.asDto();

        leagueTwo = League.builder().id(2L)
                .name("FaceIt")
                .actualRound(1)
                .rounds(Sets.newHashSet())
                .teams(Sets.newHashSet())
                .build();

        leagueTwoDto = leagueTwo.asDto();
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
}
