package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.homework.dto.JwtAuthenticationResponse;

@FeignClient(qualifiers = "JwtClient", name = "JwtClient", url = "${base.url}", configuration = JwtClientConfig.class, fallbackFactory = JwtFallbackFactory.class)
public interface JwtClient {

    @PostMapping("/jwt/tokens")
    JwtAuthenticationResponse performToken(@RequestHeader("Authorization") String basic, @RequestHeader(value = "gameId", required = false) String gameId);

    @PostMapping("/jwt/refresh")
    JwtAuthenticationResponse refreshToken(@RequestHeader("Authorization") String refreshToken);

    @PostMapping("/jwt/logout")
    Response logOut(@RequestHeader("Authorization") String refreshToken);

}
