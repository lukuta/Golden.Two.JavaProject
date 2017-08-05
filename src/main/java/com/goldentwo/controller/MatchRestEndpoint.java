package com.goldentwo.controller;

import com.goldentwo.aspect.annotation.Monitored;
import com.goldentwo.dto.MatchDto;
import com.goldentwo.exception.MatchException;
import com.goldentwo.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "matches")
public class MatchRestEndpoint {

    private MatchService matchService;

    @Autowired
    public MatchRestEndpoint(MatchService matchService) {
        this.matchService = matchService;
    }

    @Monitored
    @GetMapping()
    public List<MatchDto> findAllMatches() {
        return matchService.findAllMatches();
    }

    @GetMapping(value = "/{id}")
    public MatchDto findMatchById(@PathVariable Long id) {
        return matchService.findMatchById(id);
    }

    @PostMapping()
    public MatchDto saveMatch(@RequestBody @Valid MatchDto match) {
        return matchService.saveMatch(match);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteMatch(@PathVariable Long id) {
        return matchService.deleteMatch(id);
    }

    @ExceptionHandler(MatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(MatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Message: ", ex.getMessage());

        return response;
    }
}
