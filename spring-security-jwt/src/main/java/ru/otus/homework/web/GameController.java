package ru.otus.homework.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.dto.OrganaizeSpaceBattleRequest;
import ru.otus.homework.service.GameService;

@RestController
@RequestMapping("/game")
@Tag(name = "Игра")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Организовать космический бой")
    @PostMapping("/organaizeSpacebattle")
    public ResponseEntity<String> organaizeSpacebattle(@RequestBody OrganaizeSpaceBattleRequest organaizeSpaceBattleRequest) {
        return ResponseEntity.ok(gameService.createGame(organaizeSpaceBattleRequest.getUsers()));
    }

}
