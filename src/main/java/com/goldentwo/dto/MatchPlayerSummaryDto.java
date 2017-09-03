package com.goldentwo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayerSummaryDto {

    private String playerNickname;
    private int kills;
    private int deaths;
    private int mvps;

}
