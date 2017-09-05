package com.goldentwo.controller;

import com.goldentwo.aspect.annotation.Monitored;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.dto.MatchSummaryDto;
import com.goldentwo.dto.TurnDto;
import com.goldentwo.model.Match;
import com.goldentwo.service.MatchService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "matches")
public class MatchRestEndpoint {

    private MatchService matchService;

    @Autowired
    public MatchRestEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }

    @Monitored
    @GetMapping
    public List<MatchDto> findAllMatches() {
        return matchService.findAllMatches();
    }

    @GetMapping(value = "/{id}")
    public MatchDto findMatchById(@PathVariable Long id) {
        return matchService.findMatchById(id);
    }

    @GetMapping(value = "/{id}/summary")
    public MatchSummaryDto getMatchSummary(@PathVariable Long id) {
        return matchService.getMatchSummary(id);
    }

    @PostMapping
    public MatchDto saveMatch(@RequestBody @Valid MatchDto match) {
        return matchService.createMatch(match);
    }

    @PostMapping(value = "/{id}/turn")
    public TurnDto addTurn(@PathVariable Long id, @RequestBody TurnDto turnDto) {
        return matchService.addTurn(id, turnDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteMatch(@PathVariable Long id) {
        return matchService.deleteMatch(id);
    }

    @GetMapping(value = "/find", params = {"scoreTeamOne", "scoreTeamTwo", "ended"})
    public List<MatchDto> findMatchesBySpecificFilter(
            @Spec(params = "scoreTeamOne", path = "scoreTeamOne", spec = Equal.class) Specification<Match> scoreTeamOne,
            @Spec(params = "scoreTeamTwo", path = "scoreTeamTwo", spec = Equal.class) Specification<Match> scoreTeamTwo,
            @Spec(params = "ended", path = "ended", spec = Equal.class) Specification<Match> ended) {

        Specifications<Match> spec = Specifications
                .where(scoreTeamOne)
                .and(scoreTeamTwo)
                .and(ended);

        return matchService.findMatchesBySpecificFilter(spec);
    }
}
