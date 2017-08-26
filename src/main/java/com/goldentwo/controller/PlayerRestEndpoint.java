package com.goldentwo.controller;

import com.goldentwo.aspect.annotation.Monitored;
import com.goldentwo.dto.PlayerDto;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/players")
public class PlayerRestEndpoint {

    private PlayerService playerService;

    @Autowired
    public PlayerRestEndpoint(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Monitored
    @GetMapping
    public List<PlayerDto> findAllPlayers() {
        return playerService.findAllPlayers();
    }

    @GetMapping(value = "/{id}")
    public PlayerDto findPlayerById(@PathVariable Long id) {
        return playerService.findPlayerById(id);
    }

    @GetMapping(value = "find")
    public PlayerDto findPlayerByNickname(@RequestParam("nickname") String nickname){
        return playerService.findPlayerByNickname(nickname);
    }

    @PostMapping()
    public PlayerDto savePlayer(@RequestBody @Valid PlayerDto player) {
        return playerService.savePlayer(player);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletePlayer(@PathVariable Long id) {
        return playerService.deletePlayer(id);
    }

}
