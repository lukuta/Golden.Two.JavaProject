package com.goldentwo.dto;

import com.goldentwo.model.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Player asEntity() {
        return Player.builder().id(id).nickname(nickname).name(name).surname(surname).build();
    }
}
