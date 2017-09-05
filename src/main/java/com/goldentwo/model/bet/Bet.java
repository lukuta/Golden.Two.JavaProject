package com.goldentwo.model.bet;

import com.goldentwo.model.Match;
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
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typerNickname;

    @OneToOne(fetch = FetchType.LAZY)
    private Match match;

    private BetType betType;

    private BetStatus betStatus;
}
