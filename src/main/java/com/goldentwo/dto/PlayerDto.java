package com.goldentwo.dto;

import com.goldentwo.model.Player;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private Long id;
    @NotNull
    private String nickname;
    private String name;
    private String surname;
    private int rank;

    public Player asEntity() {
        return Player.builder().id(id).nickname(nickname).name(name).surname(surname).rank(rank).build();
    }
}
