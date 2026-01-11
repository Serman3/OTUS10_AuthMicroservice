package ru.otus.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос авторизации")
public class JwtAuthorizationRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    @Schema(description = "Айди игры", example = "5b9dc595-69e2-4e3a-8606-436ba91861a2")
    private String gameId;

    public JwtAuthorizationRequest() {
    }

    public JwtAuthorizationRequest(String username, String password, String gameId) {
        this.username = username;
        this.password = password;
        this.gameId = gameId;
    }

    public @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов") @NotBlank(message = "Имя пользователя не может быть пустыми") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов") @NotBlank(message = "Имя пользователя не может быть пустыми") String username) {
        this.username = username;
    }

    public @Size(max = 255, message = "Длина пароля должна быть не более 255 символов") String getPassword() {
        return password;
    }

    public void setPassword(@Size(max = 255, message = "Длина пароля должна быть не более 255 символов") String password) {
        this.password = password;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}
