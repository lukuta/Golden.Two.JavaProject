package com.goldentwo.service.impl;

import com.goldentwo.cache.CacheConstants;
import com.goldentwo.exception.PlayerException;
import com.goldentwo.model.Player;
import com.goldentwo.repository.PlayerRepository;
import com.goldentwo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player findPlayerById(Long id) {
        if (!playerRepository.exists(id)) {
            throw new PlayerException("There is no player with given id");
        }

        return playerRepository.findOne(id);
    }

    @Override
    public Player findPlayerByNickname(String nickname) {
        return playerRepository
                .findByNickname(nickname)
                .orElseThrow(() -> new PlayerException("There is no player with given nickname"));
    }

    @Override
    @Cacheable(CacheConstants.PLAYERS_CACHE)
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player savePlayer(Player match) {
        return playerRepository.saveAndFlush(match);
    }

    @Override
    public ResponseEntity<?> deletePlayer(Long id) {
        playerRepository.delete(id);

        return ResponseEntity.ok().build();
    }

}
