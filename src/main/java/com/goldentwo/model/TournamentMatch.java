package com.goldentwo.model;

import com.goldentwo.dto.TournamentMatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Match match;

    @Column(nullable = false)
    private double round;

    private Long nextRoundId;

    public TournamentMatchDto asDto() {
        return TournamentMatchDto.builder()
                .id(id).match(match != null ? match.asDto() : null)
                .round(round).nextRoundId(nextRoundId)
                .build();
    }

}
