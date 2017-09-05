package com.goldentwo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSummaryDto {

    private Long matchId;
    private TeamDto teamOne;
    private TeamDto teamTwo;
    private int scoreTeamOne;
    private int scoreTeamTwo;
    private boolean ended;
    private Long winnerId;
    private Set<MatchPlayerSummaryDto> teamOneStatistics;
    private Set<MatchPlayerSummaryDto> teamTwoStatistics;

}
