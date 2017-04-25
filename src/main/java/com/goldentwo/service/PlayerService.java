package com.goldentwo.service;

import com.goldentwo.model.Player;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {
    Player findPlayerById(Long id);

    Player findPlayerByNickname(String nickname);

    List<Player> findAllPlayers();

    Player savePlayer(Player match);

    ResponseEntity<?> deletePlayer(Long id);
}
