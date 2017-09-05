package com.goldentwo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto {

    private Long id;
    private String name;
    private Set<TeamDto> teams;
    private Set<TournamentMatchDto> tournamentMatches;
}
