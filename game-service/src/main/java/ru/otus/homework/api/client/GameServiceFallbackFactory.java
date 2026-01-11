package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class GameServiceFallbackFactory implements FallbackFactory<GameServiceClient> {

    @Override
    public GameServiceClient create(Throwable cause) {
        return new GameServiceClient() {
            @Override
            public Response gameAction(String bearerToken) {
                return null;
            }
        };
    }

}
