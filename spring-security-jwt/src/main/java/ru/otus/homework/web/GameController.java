package ru.otus.homework.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.datasource.dao.SpaceBattleDao;
import ru.otus.homework.dto.OrganaizeSpaceBattleRequest;

import java.util.UUID;

@RestController
@RequestMapping("/game")
@Tag(name = "Игра")
public class GameController {

    private final SpaceBattleDao spaceBattleDao;

    @Autowired
    public GameController(SpaceBattleDao spaceBattleDao) {
        this.spaceBattleDao = spaceBattleDao;
    }

    @Operation(summary = "Организовать космический бой")
    @PostMapping("/organaizeSpacebattle")
    public ResponseEntity<String> organaizeSpacebattle(@RequestBody OrganaizeSpaceBattleRequest organaizeSpaceBattleRequest) {
        UUID gameId = UUID.randomUUID();
        spaceBattleDao.addSpaceBattle(gameId, organaizeSpaceBattleRequest.getUsers());
        return ResponseEntity.ok(gameId.toString());
    }

}
