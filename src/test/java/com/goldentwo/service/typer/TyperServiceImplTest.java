package com.goldentwo.service.typer;

import com.goldentwo.model.Typer;
import com.goldentwo.repository.TyperRepository;
import com.goldentwo.service.impl.TyperServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
public class TyperServiceImplTest {

    @Mock
    private TyperRepository typerRepository;

    @InjectMocks
    private TyperServiceImpl sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTyperRankingShouldReturnSortedTyperList() {
        List<Typer> typerList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            typerList.add(Typer.builder().id((long) i).points(i).typer("typer" + i).build());
        }
        Mockito
                .when(typerRepository.findAll())
                .thenReturn(typerList);

        List<Typer> typerRankingFromSut = sut.getTyperRanking();

        typerList.sort(Comparator.comparing(Typer::getPoints).reversed());
        assertThat(typerRankingFromSut)
                .isEqualTo(typerList);
    }
}
