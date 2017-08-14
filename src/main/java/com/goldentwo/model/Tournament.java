package com.goldentwo.model;

import com.goldentwo.dto.TeamDto;
import com.goldentwo.dto.TournamentDto;
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

    @OneToMany
    private Set<Team> teams = new HashSet<>();

    public TournamentDto asDto() {
        Set<TeamDto> teamDtos = teams.stream()
                .map(Team::asDto)
                .collect(Collectors.toSet());

        return TournamentDto.builder()
                .id(id).name(name).teams(teamDtos).build();
    }

}
