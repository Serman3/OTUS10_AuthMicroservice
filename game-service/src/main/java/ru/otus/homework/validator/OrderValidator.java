package ru.otus.homework.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.otus.homework.ex.OrderParseException;
import ru.otus.homework.security.token.Token;
import ru.otus.homework.service.GameService;
import ru.otus.homework.web.dto.OrderRequest;

import java.util.List;

@Component
public class OrderValidator extends BaseValidator {

    private final GameService gameService;

    @Autowired
    public OrderValidator(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(OrderRequest.class);
    }

    public void validate(Token token, Object target, Errors errors) {
        validate(target, errors);
        List<String> usersInGame = gameService.getUsersByGameId(token.gameId());
        usersInGame.stream()
                .filter(u -> u.equals(token.subject()))
                .findFirst()
                .orElseThrow(() -> new OrderParseException("Игрок " + token.subject() + " не является участником игры " + token.gameId()));
    }

    @Override
    public void validate(Object target, Errors errors) {
        checkErrors(errors, OrderParseException::new);
    }
}
