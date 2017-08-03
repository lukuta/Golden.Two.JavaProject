package com.goldentwo.controller;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.model.Match;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class MatchRestEndpointJsonMapperTest {

    @Autowired
    private JacksonTester<MatchDto> matchJacksonTester;

    @Test
    public void testSerialize() throws IOException {
        MatchDto match = MatchDto.builder().id(10L).build();

        File expectedMatch = new ClassPathResource("expected-match.json").getFile();
        JsonContent<MatchDto> converted = this.matchJacksonTester.write(match);

        assertThat(converted)
                .isEqualToJson(expectedMatch, JSONCompareMode.STRICT);
    }
}
