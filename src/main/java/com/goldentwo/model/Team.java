package com.goldentwo.model;

import com.goldentwo.dto.TeamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @NotEmpty
    @OneToMany
    private Set<Player> players;

    public TeamDto asDto() {
        Set<String> playerNicknames = players.stream()
                .map(Player::getNickname)
                .collect(Collectors.toSet());

        return TeamDto.builder()
                .id(id)
                .name(name)
                .playerNicknames(playerNicknames)
                .build();
    }
}

