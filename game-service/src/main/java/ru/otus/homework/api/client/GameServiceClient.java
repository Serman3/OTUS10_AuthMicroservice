package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(qualifiers = "GameClient", name = "game-service", configuration = ClientConfig.class, fallbackFactory = AuthFallbackFactory.class)
public interface GameServiceClient {

    @PostMapping("/api/game/action")
    Response gameAction(@RequestHeader("Authorization") String bearerToken);
}
