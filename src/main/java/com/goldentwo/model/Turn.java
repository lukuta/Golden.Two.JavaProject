package com.goldentwo.model;

import com.goldentwo.dto.TurnDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.EntityType;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int no;

    @ElementCollection
    @CollectionTable(name="turn_kills")
    private Map<String, Integer> kills;

    @ElementCollection
    @CollectionTable(name="turn_deaths")
    private Set<String> deaths;

    @Column(nullable = false)
    private Long winner;

    @Column(nullable = false)
    private TurnWinType winType;

    public TurnDto asDto() {
        return TurnDto.builder().id(id).no(no).kills(kills).deaths(deaths).winner(winner).winType(winType).build();
    }

}
