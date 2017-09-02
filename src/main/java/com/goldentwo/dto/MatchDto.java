package com.goldentwo.dto;

import com.goldentwo.model.Match;
import com.goldentwo.model.Turn;
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
public class MatchDto {
    private Long id;
    private TeamDto teamOne;
    private TeamDto teamTwo;
    private int scoreTeamOne;
    private int scoreTeamTwo;
    private Set<TurnDto> turns;
    private boolean ended;

    public Match asEntity() {
        Set<Turn> turnSet = turns.stream().map(TurnDto::asEntity).collect(Collectors.toSet());

        return Match.builder().id(id).teamOne(teamOne.asEntity()).teamTwo(teamTwo.asEntity())
                .scoreTeamOne(scoreTeamOne).scoreTeamTwo(scoreTeamTwo).turns(turnSet).ended(ended).build();
    }
}
