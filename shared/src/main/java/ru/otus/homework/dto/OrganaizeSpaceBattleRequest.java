package ru.otus.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Организовать космический бой")
public class OrganaizeSpaceBattleRequest {

    @Schema(description = "Список пользователей", example = "username1, username2")
    private List<String> users;

    public OrganaizeSpaceBattleRequest() {
    }

    public OrganaizeSpaceBattleRequest(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

}
