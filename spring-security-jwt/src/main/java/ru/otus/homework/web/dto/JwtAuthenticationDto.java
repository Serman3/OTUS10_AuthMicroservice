package ru.otus.homework.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ c токеном доступа")
public class JwtAuthenticationDto {

    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String accessToken;

    @Schema(description = "Дата завершения работы токена доступа", example = "2026-01-09T12:10:17.412416Z")
    private String accessTokenExpiry;

    @Schema(description = "Рефреш токен", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String refreshToken;

    @Schema(description = "Дата завершения работы рефреш токен", example = "2026-01-09T12:10:17.412416Z")
    private String refreshTokenExpiry;

    public JwtAuthenticationDto() {
    }

    public JwtAuthenticationDto(String accessToken, String accessTokenExpiry, String refreshToken, String refreshTokenExpiry) {
        this.accessToken = accessToken;
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    public void setAccessTokenExpiry(String accessTokenExpiry) {
        this.accessTokenExpiry = accessTokenExpiry;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(String refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }
}
