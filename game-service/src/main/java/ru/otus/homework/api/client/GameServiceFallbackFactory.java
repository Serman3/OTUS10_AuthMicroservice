package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.otus.homework.web.dto.OrderRequest;
import ru.otus.homework.web.dto.OrderResponse;

@Profile("test")
@Component
public class GameServiceFallbackFactory implements FallbackFactory<GameServiceClient> {

    @Override
    public GameServiceClient create(Throwable cause) {
        return new GameServiceClient() {
            @Override
            public Response gameAction(String bearerToken) {
                return null;
            }

            @Override
            public OrderResponse orderAction(String bearerToken, OrderRequest orderRequest) {
                return new OrderResponse();
            }
        };
    }

}
