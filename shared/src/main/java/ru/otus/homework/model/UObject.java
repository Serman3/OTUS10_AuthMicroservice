package ru.otus.homework.model;

public interface UObject {

    void setProperty(String propertyName, Object value);

    Object getProperty(String name);

    String getId();
}
