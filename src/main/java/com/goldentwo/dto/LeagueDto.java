package com.goldentwo.dto;

import com.goldentwo.model.League;
import com.goldentwo.model.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDto {

    private Long id;
    private String name;
    private int actualRound;
    private Set<RoundDto> rounds;

    public League asEntity() {
        Set<Round> roundSet = rounds.stream().map(RoundDto::asEntity).collect(Collectors.toSet());

        return League.builder().id(id).name(name).actualRound(actualRound).rounds(roundSet).build();
    }

}
