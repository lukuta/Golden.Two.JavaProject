package com.goldentwo.controller;

import com.goldentwo.aspect.annotation.Monitored;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.model.Player;
import com.goldentwo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<Player> findAllPlayers() {
        return playerService.findAllPlayers();
    }

    @GetMapping(value = "find/{id}")
    public Player findPlayerById(@PathVariable Long id) {
        return playerService.findPlayerById(id);
    }

    @GetMapping(value = "find")
    public Player findPlayerByNickname(@RequestParam("nickname") String nickname){
        return playerService.findPlayerByNickname(nickname);
    }

    @PostMapping()
    public Player savePlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        return playerService.deletePlayer(id);
    }

    @ExceptionHandler(PlayerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleException(PlayerException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("Message: ", ex.getMessage());

        return response;
    }
}
