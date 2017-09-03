package com.goldentwo.dto;

import com.goldentwo.model.Match;
import com.goldentwo.model.Round;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundDto {

    private Long id;
    private int no;
    private Set<MatchDto> matches;

    public Round asEntity() {
        Set<Match> matchSet = matches.stream().map(MatchDto::asEntity).collect(Collectors.toSet());

        return Round.builder().id(id).no(no).matches(matchSet).build();
    }

}
