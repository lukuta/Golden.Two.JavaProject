package com.goldentwo.model.bet;

import com.goldentwo.model.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typer;

    @OneToOne(fetch = FetchType.LAZY)
    private Match match;

    private BetType betType;

    private BetStatus betStatus;
}
