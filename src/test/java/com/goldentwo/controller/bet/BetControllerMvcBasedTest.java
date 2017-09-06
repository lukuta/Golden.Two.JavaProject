package com.goldentwo.controller.bet;

import com.goldentwo.controller.BetRestController;
import com.goldentwo.model.bet.Bet;
import com.goldentwo.model.bet.BetStatus;
import com.goldentwo.repository.BettingRepository;
import com.goldentwo.repository.MatchRepository;
import com.goldentwo.service.BetService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest({BetRestController.class, BetService.class})
public class BetControllerMvcBasedTest {

    private static final String USER_NAME = "test";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BettingRepository bettingRepository;

    @MockBean
    private MatchRepository matchRepository;

    @Test
    public void ensureThatOwnBestAreReturnedProperly() throws Exception {
        List<Bet> betList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            betList.add(
                    Bet.builder()
                            .betStatus(BetStatus.OPEN)
                            .id((long) i)
                            .typer(USER_NAME + ((i % 2 == 0) ? "" : "user"))
                            .build()
            );
        }

        Mockito
                .when(bettingRepository.findAllByTyper(USER_NAME))
                .thenReturn(betList.stream().filter(bet -> bet.getTyper().equals(USER_NAME)).collect(Collectors.toList()));

        mockMvc.perform(
                get("/bets/my").accept(MediaType.APPLICATION_JSON)
                .principal(() -> USER_NAME)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}
