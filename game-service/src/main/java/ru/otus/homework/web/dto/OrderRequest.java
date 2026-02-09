package ru.otus.homework.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

@Schema(description = "Приказа на игровое действие")
public class OrderRequest {

    @Schema(description = "ID объекта, которому адресован приказ", example = "a06ede85-34f4-4c74-b98d-df482f033a10")
    @NotBlank(message = "ID объекта не может быть пустым")
    private String gameObjectId;

    @Schema(description = "ID пользователя", example = "89999999999")
    @NotBlank(message = "ID пользователя не может быть пустым")
    private String userId;

    @Schema(description = "Действие, которое необходимо выполнить", example = "StartMove")
    @NotBlank(message = "Действие не может быть пустым")
    private String actionId;

    @Schema(description = "Какие-то специфичные параметры для данного приказа")
    private Map<String, Object> args;

    public OrderRequest() {}

    public OrderRequest(String gameObjectId,
                        String userId,
                        String actionId,
                        Map<String, Object> args) {
        this.gameObjectId = gameObjectId;
        this.userId = userId;
        this.actionId = actionId;
        this.args = args;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getId() {
        return gameObjectId;
    }

    public void setId(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

}
