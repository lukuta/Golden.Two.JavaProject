package com.goldentwo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotEmpty
    private Set<String> playerNicknames;
}
