package com.goldentwo.dto;

import com.goldentwo.model.Turn;
import com.goldentwo.model.TurnWinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnDto {

    private Long id;
    private int no;
    private Map<String, Integer> kills;
    private Set<String> deaths;
    private Long winner;
    private TurnWinType winType;

    public Turn asEntity() {
        return Turn.builder().id(id).no(no).kills(kills).deaths(deaths).winner(winner).winType(winType).build();
    }
}
