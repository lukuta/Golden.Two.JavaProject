package com.goldentwo.controller.match;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.dto.TeamDto;
import com.google.common.collect.Sets;
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
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RunWith(SpringRunner.class)
public class MatchRestEndpointJsonMapperTest {

    @Autowired
    private JacksonTester<MatchDto> matchJacksonTester;

    @Test
    public void testSerialize() throws IOException {

        PlayerDto playerOne = PlayerDto.builder()
                .id(1L)
                .nickname("Taz")
                .name("Wiktor")
                .surname("Wojtas")
                .rankPoints(1)
                .build();

        PlayerDto playerTwo = PlayerDto.builder()
                .id(2L)
                .nickname("Neo")
                .name("Filip")
                .surname("Kubski")
                .rankPoints(2)
                .build();

        PlayerDto playerThree = PlayerDto.builder()
                .id(3L)
                .nickname("olofmeister")
                .name("Olof")
                .surname("Kyaber")
                .rankPoints(3)
                .build();

        PlayerDto playerFour = PlayerDto.builder()
                .id(4L)
                .nickname("JW")
                .name("Jaspher")
                .surname("Wild")
                .rankPoints(4)
                .build();

        TeamDto teamOne = TeamDto.builder()
                .id(1L)
                .name("Virtus.Pro")
                .players(Sets.newHashSet(playerOne, playerTwo))
                .rankPoints(1)
                .build();

        TeamDto teamTwo = TeamDto.builder()
                .id(2L)
                .name("Fnatic")
                .players(Sets.newHashSet(playerThree, playerFour))
                .rankPoints(2)
                .build();


        MatchDto match = MatchDto.builder()
                .id(10L)
                .ended(false)
                .teamOne(teamOne)
                .teamTwo(teamTwo)
                .scoreTeamOne(13)
                .scoreTeamTwo(2)
                .turns(new HashSet<>())
                .build();

        File expectedMatch = new ClassPathResource("expected-match.json").getFile();
        JsonContent<MatchDto> converted = this.matchJacksonTester.write(match);

        assertThat(converted)
                .isEqualToJson(expectedMatch, JSONCompareMode.LENIENT);
    }
}
