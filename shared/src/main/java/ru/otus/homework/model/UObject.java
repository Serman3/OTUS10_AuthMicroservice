package ru.otus.homework.model;

import java.util.Map;

public interface UObject {

    void setProperty(String propertyName, Object value);

    Object getProperty(String name);

    String getId();

    Map<String, Object> getProperties();
}
