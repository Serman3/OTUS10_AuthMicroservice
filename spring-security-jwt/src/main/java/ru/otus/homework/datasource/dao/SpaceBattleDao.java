package ru.otus.homework.datasource.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class SpaceBattleDao {

    private static final Map<UUID, List<String>> SPACE_BATTLE_GAMES = new HashMap<>();

    public void addSpaceBattle(UUID gameId, List<String> users) {
        SPACE_BATTLE_GAMES.put(gameId, users);
    }

    public List<String> getUsersByGameId(UUID gameId) {
        return SPACE_BATTLE_GAMES.get(gameId);
    }
}
