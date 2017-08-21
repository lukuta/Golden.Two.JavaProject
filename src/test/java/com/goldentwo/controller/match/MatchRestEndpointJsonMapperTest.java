package com.goldentwo.controller.match;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TeamDto;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RunWith(SpringRunner.class)
public class MatchRestEndpointJsonMapperTest {

    @Autowired
    private JacksonTester<MatchDto> matchJacksonTester;

    @Test
    public void testSerialize() throws IOException {
        TeamDto teamOne = TeamDto.builder()
                .id(1L)
                .name("Virtus.Pro")
                .build();

        TeamDto teamTwo = TeamDto.builder()
                .id(2L)
                .name("Fnatic")
                .build();


        MatchDto match = MatchDto.builder()
                .id(10L)
                .ended(false)
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .scoreTeamOne(13)
                .scoreTeamTwo(2)
                .build();

        File expectedMatch = new ClassPathResource("expected-match.json").getFile();
        JsonContent<MatchDto> converted = this.matchJacksonTester.write(match);

        assertThat(converted)
                .isEqualToJson(expectedMatch, JSONCompareMode.STRICT);
    }
}
