package com.goldentwo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMatchDto {

    private Long id;
    private MatchDto match;
    private double round;
    private Long nextRoundId;

}
