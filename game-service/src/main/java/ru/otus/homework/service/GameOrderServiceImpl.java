package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.command.Command;
import ru.otus.homework.game.CommandFactory;
import ru.otus.homework.model.UObject;
import ru.otus.homework.storage.GameContext;
import ru.otus.homework.model.Order;

import java.util.Map;

@Service
public class GameOrderServiceImpl implements GameOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOrderServiceImpl.class);

    private final GameStorage gameStorage;
    private final CommandFactory commandFactory;
    private final BeanFactory beanFactory;

    @Autowired
    public GameOrderServiceImpl(GameStorage gameStorage,
                                CommandFactory commandFactory,
                                BeanFactory beanFactory) {
        this.gameStorage = gameStorage;
        this.commandFactory = commandFactory;
        this.beanFactory = beanFactory;
    }

    @Override
    public Map<String, Object> orderAction(String userId, String gameId, Order order) {
        LOGGER.info("Process order: {}", order.toString());

        try {
            GameContext gameContext = gameStorage.resolveGame(gameId);

            Command interpretCommand = (Command) beanFactory.getBean(
                    "interpretCommand",
                    order.getId(),
                    userId,
                    order.getActionId(),
                    order.getArgs(),
                    commandFactory,
                    gameContext
            );

            interpretCommand.execute();

            gameContext.getCommandQueue(userId).take().execute();
            UObject uObject = gameContext.getGameObject(userId, order.getId());
            return uObject.getProperties();
        } catch (Throwable e) {
            LOGGER.error("Process order error", e);
            throw new RuntimeException(e);
        }
    }

}