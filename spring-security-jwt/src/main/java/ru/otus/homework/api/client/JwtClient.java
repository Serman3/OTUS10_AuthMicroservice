package ru.otus.homework.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.homework.web.dto.JwtAuthenticationDto;

@FeignClient(qualifiers = "JwtClient", name = "JwtClient", url = "${base.url}", configuration = JwtClientConfig.class, fallbackFactory = JwtFallbackFactory.class)
public interface JwtClient {

    @PostMapping("/jwt/tokens")
    JwtAuthenticationDto performToken(@RequestHeader("Authorization") String basic);

    @PostMapping("/jwt/refresh")
    JwtAuthenticationDto refreshToken(@RequestHeader("Authorization") String refreshToken);

    @PostMapping("/jwt/logout")
    Response logOut(@RequestHeader("Authorization") String refreshToken);

}
