package com.goldentwo.model;

import com.goldentwo.dto.TurnDto;
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
public class Turn {

    private Long id;
    private int no;
    private Map<Long, Long> kills;
    private Long winner;
    private TurnWinType winType;

    public TurnDto asDto() {
        return TurnDto.builder().id(id).no(no).kills(kills).winner(winner).winType(winType).build();
    }

}
