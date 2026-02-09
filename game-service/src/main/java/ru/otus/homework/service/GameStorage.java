package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.model.UObject;
import ru.otus.homework.storage.GameContext;
import ru.otus.homework.storage.GameContextImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameStorage {

    private static final Map<String, GameContext> GAMES = new ConcurrentHashMap<>();
    private final GameService gameService;
    private final List<UObject> gameItemList;

    @Autowired
    public GameStorage(GameService gameService,
                       List<UObject> gameItemList) {
        this.gameService = gameService;
        this.gameItemList = gameItemList;
    }

    public GameContext getGame(String gameId) {
        return GAMES.get(gameId);
    }

    public GameContext resolveGame(String gameId) {
        List<String> users = gameService.getUsersByGameId(gameId);
        GameContext gameContext = new GameContextImpl(gameId, gameItemList, users);
        GAMES.put(gameContext.getGameId(), gameContext);
        return getGame(gameId);
    }

}
