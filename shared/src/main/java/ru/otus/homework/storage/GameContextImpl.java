package ru.otus.homework.storage;

import org.springframework.context.ApplicationContext;
import ru.otus.homework.annotation.Id;
import ru.otus.homework.command.Command;
import ru.otus.homework.model.UObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class GameContextImpl implements GameContext {

    private final String gameId;
    private final Map<String, BlockingQueue<Command>> userCommands;
    private final Map<String, List<UObject>> userGameObjects = new ConcurrentHashMap<>();

    public GameContextImpl(String gameId,
                           ApplicationContext applicationContext,
                           List<String> users) {
        this.gameId = gameId;
        this.userCommands = users.stream().collect(Collectors.toMap(e -> e, e -> new LinkedBlockingQueue<>()));
        users.forEach(user -> {
            List<UObject> gameItemList = applicationContext.getBeansOfType(UObject.class)
                    .values()
                    .stream()
                    .filter(gi -> gi.getClass().isAnnotationPresent(Id.class))
                    .toList();
            userGameObjects.put(user, gameItemList);
        });
    }

    @Override
    public String getGameId() {
        return this.gameId;
    }

    @Override
    public List<String> getUsers() {
        return this.userCommands.keySet().stream().toList();
    }

    @Override
    public BlockingQueue<Command> getCommandQueue(String userId) {
        return userCommands.get(userId);
    }

    @Override
    public UObject getGameObject(String userId, String objectId) {
        return userGameObjects.get(userId)
                .stream()
                .filter(uObject -> Objects.equals(uObject.getId(), objectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найден объект с id " + objectId));
    }

    @Override
    public void addCommand(String userId, Command command) {
        userCommands.get(userId).add(command);
    }

}
