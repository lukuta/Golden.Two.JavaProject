package com.goldentwo.model;

import com.goldentwo.dto.MatchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Match(MatchDto matchdto) {
        this.id = matchdto.getId();
    }

    public MatchDto asDto() {
        return MatchDto.builder()
                .id(id)
                .build();
    }

}
