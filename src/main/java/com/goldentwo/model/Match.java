package com.goldentwo.model;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.TurnDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team teamOne;
    @ManyToOne
    private Team teamTwo;
    @OneToMany
    private Set<Turn> turns;

    @ColumnDefault(value = "0")
    private int scoreTeamOne;

    @ColumnDefault(value = "0")
    private int scoreTeamTwo;

    @ColumnDefault(value = "false")
    private boolean ended;

    public Match(MatchDto matchDto) {
        this.id = matchDto.getId();
    }

    public MatchDto asDto() {
        Set<TurnDto> turnDtoSet = turns != null ? turns.stream().map(Turn::asDto).collect(Collectors.toSet()) : new HashSet<>();

        return MatchDto.builder()
                .id(id)
                .teamTwo(teamTwo != null ? teamTwo.asDto() : null)
                .teamOne(teamOne != null ? teamOne.asDto() : null)
                .scoreTeamTwo(scoreTeamTwo)
                .scoreTeamOne(scoreTeamOne)
                .ended(ended)
                .turns(turnDtoSet)
                .build();
    }

}
