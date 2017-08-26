package com.goldentwo.dto;

import com.goldentwo.model.League;
import com.goldentwo.model.Round;
import com.goldentwo.model.Team;
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
    private Set<TeamDto> teams;

    public League asEntity() {
        Set<Round> roundSet = rounds.stream().map(RoundDto::asEntity).collect(Collectors.toSet());
        Set<Team> teamSet = teams.stream().map(TeamDto::asEntity).collect(Collectors.toSet());

        return League.builder().id(id).name(name)
                .actualRound(actualRound).rounds(roundSet)
                .teams(teamSet).build();
    }

}
