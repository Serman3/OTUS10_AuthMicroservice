package ru.otus.homework.game.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.homework.command.Command;
import ru.otus.homework.game.CommandFactory;
import ru.otus.homework.storage.GameContext;
import ru.otus.homework.model.UObject;

import java.util.Map;

@Component
@Scope("prototype")
public class InterpretCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterpretCommand.class);

    private final String gameObjectId;
    private final String userId;
    private final String actionId;
    private final Map<String, Object> args;
    private final CommandFactory commandFactory;
    private final GameContext gameContext;

    public InterpretCommand(
            String gameObjectId,
            String userId,
            String actionId,
            Map<String, Object> args,
            CommandFactory commandFactory,
            GameContext gameContext) {
        this.gameObjectId = gameObjectId;
        this.userId = userId;
        this.actionId = actionId;
        this.args = args;
        this.commandFactory = commandFactory;
        this.gameContext = gameContext;
    }

    @Override
    public void execute() {
        try {
            UObject gameObject = gameContext.getGameObject(gameObjectId);
            if (gameObject == null) {
                throw new IllegalArgumentException("Game object not found: " + gameObjectId);
            }

            Command command = commandFactory.createCommand(actionId, gameObject, args);

            gameContext.addCommand(userId, command);
        } catch (Exception e) {
            LOGGER.error("Error in InterpretCommand execution: {}", e.getMessage(), e);
            throw e;
        }
    }
}
