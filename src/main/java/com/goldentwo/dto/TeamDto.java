package com.goldentwo.dto;

import com.goldentwo.model.Player;
import com.goldentwo.model.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    private Long id;
    @NotNull
    private String name;
    @NotEmpty
    private Set<PlayerDto> players;
    @Column(unique = true)
    @ColumnDefault(value = "0")
    private int rank;

    public Team asEntity() {
        Set<Player> playerSet = players.stream()
                .map(PlayerDto::asEntity)
                .collect(Collectors.toSet());

        return Team.builder().id(id).name(name).players(playerSet).rank(rank).build();
    }
}
