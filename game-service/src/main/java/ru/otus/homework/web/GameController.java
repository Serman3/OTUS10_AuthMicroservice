package ru.otus.homework.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.service.GameOrderService;
import ru.otus.homework.validator.OrderValidator;
import ru.otus.homework.web.dto.OrderRequest;
import ru.otus.homework.model.Order;
import ru.otus.homework.web.dto.OrderResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@Tag(name = "Апи космических боев")
@Profile("prod")
public class GameController {

    private final OrderValidator orderValidator;
    private final ModelMapper modelMapper;
    private final GameOrderService gameOrderService;

    @Autowired
    public GameController(OrderValidator orderValidator,
                          ModelMapper modelMapper,
                          GameOrderService gameOrderService) {
        this.orderValidator = orderValidator;
        this.modelMapper = modelMapper;
        this.gameOrderService = gameOrderService;
    }

    @PostMapping("/action")
    @Operation(summary = "Запрос заглушка для проверки авторизованного пользователя")
    public ResponseEntity<Map<String, String>> gameAction() {
        return ResponseEntity.ok(Map.of("gameAction", "gameAction"));
    }

    @PostMapping("/order")
    @Operation(summary = "Запрос приказа на действие с игровым объектом")
    public ResponseEntity<OrderResponse> orderAction(UsernamePasswordAuthenticationToken principal, @RequestBody @Valid OrderRequest orderRequest, BindingResult bindingResult) {
        orderValidator.validate(orderRequest, bindingResult);

        String gameId = principal.getCredentials().toString();

        Map<String, Object> gameObjectProperties = gameOrderService.orderAction(gameId, modelMapper.map(orderRequest, Order.class));

        return ResponseEntity.ok(new OrderResponse(orderRequest.getUserId(), orderRequest.getActionId(), orderRequest.getId(), gameObjectProperties));
}

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(Throwable exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
