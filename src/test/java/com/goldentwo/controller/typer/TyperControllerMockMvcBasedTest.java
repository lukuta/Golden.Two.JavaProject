package com.goldentwo.controller.typer;

import com.goldentwo.controller.TyperController;
import com.goldentwo.model.Typer;
import com.goldentwo.repository.TyperRepository;
import com.goldentwo.service.TyperService;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest({TyperController.class, TyperService.class})
public class TyperControllerMockMvcBasedTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TyperRepository typerRepository;

    @Test
    public void ensureThatTyperRankingAreReturnedProperly() throws Exception {
        List<Typer> typerList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            typerList.add(Typer.builder().id((long) i).points(i).typer("typer" + i).build());
        }

        Mockito
                .when(typerRepository.findAll())
                .thenReturn(typerList);

        mockMvc.perform(
                get("/typers/").accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)))
                .andExpect(jsonPath("$[0].id", is(9)))
                .andExpect(jsonPath("$[8].id", is(1)));
    }
}
