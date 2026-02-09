package ru.otus.homework.game.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.homework.annotation.Id;
import ru.otus.homework.model.UObject;

import java.util.HashMap;
import java.util.Map;

@Id("548")
@Component
@Scope("prototype")
public class SpaceBattleObject implements UObject {

    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public Object getProperty(String key) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        throw new RuntimeException("Свойство " + key + " не найдено");
    }

    @Override
    public void setProperty(String key, Object value) {
        if (key != null && value != null) {
            properties.put(key, value);
        } else {
            throw new RuntimeException("Ключ и значение не могут быть null");
        }
    }

    @Override
    public String getId() {
        return this.getClass().getAnnotation(Id.class).value();
    }

    @Override
    public String toString() {
        return "SpaceBattleObject{" +
                "id=" + getId() +
                "properties=" + properties +
                '}';
    }
}
