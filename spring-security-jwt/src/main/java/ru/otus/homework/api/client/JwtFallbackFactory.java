package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.JwtAuthenticationResponse;

@Component
public class JwtFallbackFactory implements FallbackFactory<JwtClient> {

    @Override
    public JwtClient create(Throwable cause) {
        return new JwtClient() {
            @Override
            public JwtAuthenticationResponse performToken(String basic, String gameId) {
                return null;
            }

            @Override
            public JwtAuthenticationResponse refreshToken(String refreshToken) {
                return null;
            }

            @Override
            public Response logOut(String accessToken) {
                return null;
            }
        };
    }
}
