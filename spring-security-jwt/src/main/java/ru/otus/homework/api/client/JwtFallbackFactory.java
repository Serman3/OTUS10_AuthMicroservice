package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.otus.homework.web.dto.JwtAuthenticationDto;

@Component
public class JwtFallbackFactory implements FallbackFactory<JwtClient> {

    @Override
    public JwtClient create(Throwable cause) {
        return new JwtClient() {
            @Override
            public JwtAuthenticationDto performToken(String basic) {
                return null;
            }

            @Override
            public JwtAuthenticationDto refreshToken(String refreshToken) {
                return null;
            }

            @Override
            public Response logOut(String accessToken) {
                return null;
            }
        };
    }
}
