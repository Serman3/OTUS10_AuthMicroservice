package ru.otus.homework.game.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.homework.annotation.Id;
import ru.otus.homework.command.Command;
import ru.otus.homework.model.UObject;

import java.util.Map;

@Id("StopMove")
@Component
@Scope("prototype")
public class StopMoveCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopMoveCommand.class);

    private final UObject gameObject;
    private final Map<String, Object> args;

    public StopMoveCommand(UObject gameObject,
                            Map<String, Object> args) {
        this.gameObject = gameObject;
        this.args = args;
    }

    @Override
    public void execute() {
        LOGGER.info("Stop Moving object {}", gameObject.getId());
    }

}
