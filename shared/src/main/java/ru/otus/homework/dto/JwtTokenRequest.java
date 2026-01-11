package ru.otus.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос c токеном доступа")
public class JwtTokenRequest {

    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;

    public JwtTokenRequest() {
    }

    public JwtTokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
