package com.goldentwo.dto;

import com.goldentwo.model.Turn;
import com.goldentwo.model.TurnWinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnDto {

    private Long id;
    private int no;
    private Map<Long, Long> kills;
    private Long winner;
    private TurnWinType winType;

    public Turn asEntity() {
        return Turn.builder().id(id).no(no).kills(kills).winner(winner).winType(winType).build();
    }
}
