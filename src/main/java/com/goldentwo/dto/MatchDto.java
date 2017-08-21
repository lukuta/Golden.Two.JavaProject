package com.goldentwo.dto;

import com.goldentwo.model.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean ended;

    public Match asEntity() {
        return Match.builder().id(id).teamOne(teamOne.asEntity()).teamTwo(teamTwo.asEntity())
                .scoreTeamOne(scoreTeamOne).scoreTeamTwo(scoreTeamTwo).ended(ended).build();
    }
}
