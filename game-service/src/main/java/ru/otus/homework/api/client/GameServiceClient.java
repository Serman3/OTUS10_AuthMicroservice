package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.homework.web.dto.OrderRequest;

@Profile("test")
@FeignClient(qualifiers = "GameClient", name = "GameClient", url = "${services.game-service.url}", configuration = ClientConfig.class, fallbackFactory = AuthFallbackFactory.class)
public interface GameServiceClient {

    @PostMapping("/api/game/action")
    Response gameAction(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("/api/game/order")
    Response orderAction(@RequestHeader("Authorization") String bearerToken, OrderRequest orderRequest);
}
