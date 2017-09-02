package com.goldentwo.model;

import com.goldentwo.dto.PlayerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;
    private String name;
    private String surname;
    private int rankPoints;

    public Player(PlayerDto playerDto) {
        this.nickname = playerDto.getNickname();
        this.name = playerDto.getName();
        this.surname = playerDto.getSurname();
        this.rankPoints = playerDto.getRankPoints();
    }

    public PlayerDto asDto() {
        return PlayerDto.builder()
                .id(id)
                .nickname(nickname)
                .name(name)
                .surname(surname)
                .rankPoints(rankPoints)
                .build();
    }

}
