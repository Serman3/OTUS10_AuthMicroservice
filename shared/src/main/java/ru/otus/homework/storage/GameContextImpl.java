package ru.otus.homework.storage;

import ru.otus.homework.annotation.Id;
import ru.otus.homework.command.Command;
import ru.otus.homework.model.UObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class GameContextImpl implements GameContext {

    private final String gameId;
    private final Map<String, BlockingQueue<Command>> usersCommand;
    private final Map<String, UObject> gameObjects = new ConcurrentHashMap<>();

    public GameContextImpl(
            String gameId,
            List<UObject> gameItemList,
            List<String> users) {
        this.gameId = gameId;
        this.usersCommand = users.stream().collect(Collectors.toMap(e -> e, e -> new LinkedBlockingQueue<>()));
        gameItemList.stream().filter(gi -> gi.getClass().isAnnotationPresent(Id.class)).forEach(gi -> {
            Id id = gi.getClass().getAnnotation(Id.class);
            addGameObject(id.value(), gi);
        });
    }

    @Override
    public String getGameId() {
        return this.gameId;
    }

    @Override
    public List<String> getUsers() {
        return this.usersCommand.keySet().stream().toList();
    }

    @Override
    public BlockingQueue<Command> getCommandQueue(String userId) {
        return usersCommand.get(userId);
    }

    @Override
    public UObject getGameObject(String objectId) {
        return gameObjects.get(objectId);
    }

    @Override
    public void addGameObject(String id, UObject gameItem) {
        gameObjects.put(id, gameItem);
    }

    @Override
    public void addCommand(String userId, Command command) {
        usersCommand.get(userId).add(command);
    }

}
