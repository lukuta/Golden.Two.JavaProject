package com.goldentwo.model;

import com.goldentwo.dto.LeagueDto;
import com.goldentwo.dto.RoundDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ColumnDefault(value = "1")
    private int actualRound;

    @OneToMany
    private Set<Round> rounds;

    public LeagueDto asDto() {
        Set<RoundDto> roundDtos = rounds.stream().map(Round::asDto).collect(Collectors.toSet());

        return LeagueDto.builder().id(id).name(name).actualRound(actualRound).rounds(roundDtos).build();
    }

}
