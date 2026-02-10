package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.otus.homework.storage.GameContext;
import ru.otus.homework.storage.GameContextImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameStorage {

    private static final Map<String, GameContext> GAMES = new ConcurrentHashMap<>();
    private final GameService gameService;
    private final ApplicationContext applicationContext;

    @Autowired
    public GameStorage(GameService gameService,
                       ApplicationContext applicationContext) {
        this.gameService = gameService;
        this.applicationContext = applicationContext;
    }

    public GameContext resolveGame(String gameId) {
        if (GAMES.containsKey(gameId)) {
            return getGame(gameId);
        }
        GAMES.put(gameId, new GameContextImpl(gameId, applicationContext, gameService.getUsersByGameId(gameId)));
        return getGame(gameId);
    }

    private GameContext getGame(String gameId) {
        return GAMES.get(gameId);
    }

}
