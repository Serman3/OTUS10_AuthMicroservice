package ru.otus.homework.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Ответ на действие игрового приказа")
public class OrderResponse {

    @JsonProperty("executed_command_user_id")
    @Schema(description = "ID пользователя, вызвавший команду", example = "username")
    private String executedCommandUserId;

    @JsonProperty("action_id_command")
    @Schema(description = "Команда, которая была выполнена", example = "StartMove")
    private String completedActionId;

    @JsonProperty("game_object_id")
    @Schema(description = "ID объекта, которому адресован приказ", example = "432")
    private String gameObjectId;

    @Schema(description = "Какие-то специфичные параметры для данного приказа")
    private Map<String, Object> propertiesGameObject;

    public OrderResponse() {
    }

    public OrderResponse(String executedCommandUserId,
                         String completedActionId,
                         String gameObjectId,
                         Map<String, Object> propertiesGameObject) {
        this.executedCommandUserId = executedCommandUserId;
        this.completedActionId = completedActionId;
        this.gameObjectId = gameObjectId;
        this.propertiesGameObject = propertiesGameObject;
    }

    public String getGameObjectId() {
        return gameObjectId;
    }

    public void setGameObjectId(String gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public String getExecutedCommandUserId() {
        return executedCommandUserId;
    }

    public void setExecutedCommandUserId(String executedCommandUserId) {
        this.executedCommandUserId = executedCommandUserId;
    }

    public String getCompletedActionId() {
        return completedActionId;
    }

    public void setCompletedActionId(String completedActionId) {
        this.completedActionId = completedActionId;
    }

    public Map<String, Object> getPropertiesGameObject() {
        return propertiesGameObject;
    }

    public void setPropertiesGameObject(Map<String, Object> propertiesGameObject) {
        this.propertiesGameObject = propertiesGameObject;
    }
}
