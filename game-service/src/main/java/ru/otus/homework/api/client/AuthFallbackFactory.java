package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.otus.homework.dto.*;

@Component
public class AuthFallbackFactory implements FallbackFactory<AuthServiceClient> {

    @Override
    public AuthServiceClient create(Throwable cause) {
        return new AuthServiceClient() {
            @Override
            public JwtAuthenticationResponse authorize(JwtAuthorizationRequest jwtAuthorizationRequest) {
                return null;
            }

            @Override
            public Response register(RegistrationRequest registrationRequest) {
                return null;
            }

            @Override
            public JwtAuthenticationResponse refresh(JwtTokenRequest jwtTokenRequest) {
                return null;
            }

            @Override
            public Response logout(JwtTokenRequest jwtTokenRequest) {
                return null;
            }

            @Override
            public String organaizeSpacebattle(String accessToken, OrganaizeSpaceBattleRequest organaizeSpaceBattleRequest) {
                return "";
            }
        };
    }

}
