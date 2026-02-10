package ru.otus.homework.storage;

import ru.otus.homework.command.Command;
import ru.otus.homework.model.UObject;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface GameContext {

    String getGameId();

    List<String> getUsers();

    BlockingQueue<Command> getCommandQueue(String userId);

    void addCommand(String userId, Command command);

    UObject getGameObject(String userId, String objectId);

}
