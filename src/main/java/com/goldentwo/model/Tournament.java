package com.goldentwo.model;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
import com.goldentwo.dto.TournamentMatchDto;
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
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    private Set<Team> teams = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TournamentMatch> matches = new HashSet<>();

    public TournamentDto asDto() {
        Set<TeamDto> teamDtos = teams.stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());

        Set<TournamentMatchDto> tournamentMatchDtos = matches.stream()
                .map(TournamentMatch::asDto)
                .collect(Collectors.toSet());

        return TournamentDto.builder()
                .id(id).name(name)
                .teams(teamDtos)
                .tournamentMatches(tournamentMatchDtos)
                .build();
    }

}
