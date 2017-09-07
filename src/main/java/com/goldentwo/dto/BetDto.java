package com.goldentwo.dto;

import com.goldentwo.model.bet.BetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetDto {
    private Long matchId;
    private BetType betType;
}
