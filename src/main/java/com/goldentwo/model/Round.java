package com.goldentwo.model;

import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.RoundDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int no;
    @OneToMany
    private Set<Match> matches;

    public RoundDto asDto() {
        Set<MatchDto> matchDtos = matches.stream().map(Match::asDto).collect(Collectors.toSet());

        return RoundDto.builder().id(id).no(no).matches(matchDtos).build();
    }

}
