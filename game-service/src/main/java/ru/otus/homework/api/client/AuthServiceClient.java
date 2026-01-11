package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.homework.dto.*;

@FeignClient(qualifiers = "AuthClient", name = "AuthClient", url = "${services.auth.url}", configuration = ClientConfig.class, fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

    @PostMapping("/auth/authorize")
    JwtAuthenticationResponse authorize(JwtAuthorizationRequest jwtAuthorizationRequest);

    @PostMapping("/auth/register")
    Response register(RegistrationRequest registrationRequest);

    @PostMapping("/auth/refresh")
    JwtAuthenticationResponse refresh(JwtTokenRequest jwtTokenRequest);

    @PostMapping("/auth/logout")
    Response logout(JwtTokenRequest jwtTokenRequest);

    @PostMapping("/game/organaizeSpacebattle")
    String organaizeSpacebattle(@RequestHeader("Authorization") String accessToken, OrganaizeSpaceBattleRequest organaizeSpaceBattleRequest);

}
