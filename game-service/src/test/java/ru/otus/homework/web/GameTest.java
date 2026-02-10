package ru.otus.homework.web;

import feign.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.homework.api.client.AuthServiceClient;
import ru.otus.homework.api.client.GameServiceClient;
import ru.otus.homework.dto.JwtAuthenticationResponse;
import ru.otus.homework.dto.JwtAuthorizationRequest;
import ru.otus.homework.dto.OrganaizeSpaceBattleRequest;
import ru.otus.homework.dto.RegistrationRequest;
import ru.otus.homework.web.dto.OrderRequest;
import ru.otus.homework.web.dto.OrderResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles(value = "test")
public class GameTest {

    private static final Map<String, String> CREDENTIALS = Map.of(
            "username1", "password1",
            "username2", "password2"
    );

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private GameServiceClient gameServiceClient;

    private final Map<String, String> userToken = new HashMap<>();

    @BeforeAll
    public static void init(@Autowired AuthServiceClient authServiceClient) {
        CREDENTIALS.forEach((key, value) -> authServiceClient.register(new RegistrationRequest(key, value)));
    }

    @BeforeEach
    public void authAndCreateGame() {
        JwtAuthorizationRequest jwtAuthorizationRequest = new JwtAuthorizationRequest();
        jwtAuthorizationRequest.setUsername(CREDENTIALS.entrySet().iterator().next().getKey());
        jwtAuthorizationRequest.setPassword(CREDENTIALS.entrySet().iterator().next().getValue());

        JwtAuthenticationResponse jwtAuthenticationResponse = authServiceClient.authorize(jwtAuthorizationRequest);

        String gameId = authServiceClient.organaizeSpacebattle(
                "Bearer " + jwtAuthenticationResponse.getAccessToken(),
                new OrganaizeSpaceBattleRequest(CREDENTIALS
                        .keySet()
                        .stream()
                        .toList()
                ));

        CREDENTIALS.forEach((key, value) -> {
            JwtAuthenticationResponse authenticationResponse = authServiceClient.authorize(new JwtAuthorizationRequest(key, value, gameId));
            userToken.put(key, authenticationResponse.getAccessToken());
        });
    }

    @Test
    public void authTest() {
        userToken.forEach((key, value) -> {
            Response response = gameServiceClient.gameAction("Bearer " + value);
            assertEquals(200, response.status());
        });
    }

    @Test
    public void orderActionTest() {
        userToken.forEach((key, value) -> {
            String token = "Bearer " + value;

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setUserId(key);
            orderRequest.setId("548");

            orderRequest.setArgs(Map.of("point", 2));
            orderRequest.setActionId("StartMove");
            OrderResponse response = gameServiceClient.orderAction(token, orderRequest);
            assertEquals(2, (Integer) response.getPropertiesGameObject().get("point"));

            orderRequest.setArgs(Map.of());
            orderRequest.setActionId("StopMove");
            response = gameServiceClient.orderAction(token, orderRequest);
            assertTrue((Boolean) response.getPropertiesGameObject().get("stopped"));

            orderRequest.setArgs(Map.of("shotToPoint", 15));
            orderRequest.setActionId("StartShot");
            response = gameServiceClient.orderAction(token, orderRequest);
            assertEquals(15, (Integer) response.getPropertiesGameObject().get("shotToPoint"));
        });
    }

}