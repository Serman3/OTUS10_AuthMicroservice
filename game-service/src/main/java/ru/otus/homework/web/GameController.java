package ru.otus.homework.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@Tag(name = "Апи космических боев")
@Profile("prod")
public class GameController {

    @PostMapping("/action")
    @Operation(summary = "Запрос заглушка для проверки авторизованного пользователя")
    public ResponseEntity<Map<String, String>> gameAction() {
        return ResponseEntity.ok(Map.of("gameAction","gameAction"));
    }
}
