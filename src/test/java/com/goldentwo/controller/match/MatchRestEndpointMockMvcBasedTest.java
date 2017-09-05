package com.goldentwo.controller.match;

import com.goldentwo.controller.MatchRestEndpoint;
import com.goldentwo.model.Match;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.repository.TeamRepository;
import com.goldentwo.repository.TurnRepository;
import com.goldentwo.service.MatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest({MatchRestEndpoint.class, MatchService.class})
public class MatchRestEndpointMockMvcBasedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchRepository matchRepositoryMock;

    @MockBean
    private TurnRepository turnRepositoryMock;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void ensureThatMatchesAreAddedProperly() throws Exception {
        Match match = Match.builder().id(1L).build();
        Match anotherMatch = Match.builder().id(2L).build();

        List<Match> matchList = Arrays.asList(match, anotherMatch);

        Mockito
                .when(matchRepositoryMock.findAll())
                .thenReturn(matchList);

        mockMvc.perform(
                get("/matches/").accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}

