package ru.otus.homework.web;

import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.homework.api.client.AuthServiceClient;
import ru.otus.homework.api.client.GameServiceClient;
import ru.otus.homework.dto.JwtAuthenticationResponse;
import ru.otus.homework.dto.JwtAuthorizationRequest;
import ru.otus.homework.dto.OrganaizeSpaceBattleRequest;
import ru.otus.homework.dto.RegistrationRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GameTest {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private GameServiceClient gameServiceClient;

    private final Map<String, String> credentials = Map.of(
            "username1", "password1",
            "username2", "password2"
    );

    @BeforeEach
    public void init() {
        credentials.forEach((key, value) -> authServiceClient.register(new RegistrationRequest(key, value)));
    }

    @Test
    public void organaizeGameTest() {
        JwtAuthorizationRequest jwtAuthorizationRequest = new JwtAuthorizationRequest();
        jwtAuthorizationRequest.setUsername(credentials.entrySet().iterator().next().getKey());
        jwtAuthorizationRequest.setPassword(credentials.entrySet().iterator().next().getValue());

        JwtAuthenticationResponse jwtAuthenticationResponse = authServiceClient.authorize(jwtAuthorizationRequest);

        String gameId = authServiceClient.organaizeSpacebattle(
                jwtAuthenticationResponse.getAccessToken(),
                new OrganaizeSpaceBattleRequest(credentials
                        .keySet()
                        .stream()
                        .toList()
                ));

        Map<String, String> userToken = new HashMap<>();

        credentials.forEach((key, value) -> {
            JwtAuthenticationResponse authenticationResponse = authServiceClient.authorize(new JwtAuthorizationRequest(key, value, gameId));
            userToken.put(key, authenticationResponse.getAccessToken());
        });

        userToken.forEach((key, value) -> {
            Response response = gameServiceClient.gameAction(value);
            assertEquals(200, response.status());
        });
    }

}