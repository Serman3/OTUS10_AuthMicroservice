package ru.otus.homework.model;

import java.util.HashMap;
import java.util.Map;

public class Order implements UObject {

    private final Map<String, Object> properties = new HashMap<>();

    private String gameObjectId;

    private String userId;

    private String actionId;

    private Map<String, Object> args;

    public Order() {
        initProperties();
    }

    public Order(String gameObjectId,
                 String userId,
                 String actionId,
                 Map<String, Object> args) {
        this.gameObjectId = gameObjectId;
        this.userId = userId;
        this.actionId = actionId;
        this.args = args;
        initProperties();
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public String getId() {
        return gameObjectId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
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

    private void initProperties() {
        setProperty("gameObjectId", this.getId());
        setProperty("userId", this.getUserId());
        setProperty("actionId", this.getActionId());
        setProperty("args", this.getArgs());
    }

    @Override
    public String toString() {
        return "Order{" +
                "properties=" + properties +
                ", gameObjectId='" + gameObjectId + '\'' +
                ", userId='" + userId + '\'' +
                ", actionId='" + actionId + '\'' +
                ", args=" + args +
                '}';
    }
}
