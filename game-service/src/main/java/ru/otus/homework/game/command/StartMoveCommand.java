package ru.otus.homework.game.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.homework.annotation.Id;
import ru.otus.homework.command.Command;
import ru.otus.homework.model.UObject;

import java.util.Map;

@Id("StartMove")
@Component
@Scope("prototype")
public class StartMoveCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartMoveCommand.class);

    private final UObject gameObject;
    private final Map<String, Object> args;

    public StartMoveCommand(UObject gameObject,
                            Map<String, Object> args) {
        this.gameObject = gameObject;
        this.args = args;
    }

    @Override
    public void execute() {
        Integer point = (Integer) args.get("point");
        gameObject.setProperty("point", point);
        LOGGER.info("Start Moving object {} with point {}", gameObject.getId(), point);
    }
}
