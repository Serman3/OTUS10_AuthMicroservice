package ru.otus.homework.service;

import ru.otus.homework.model.Order;

import java.util.Map;

public interface GameOrderService {

    Map<String, Object> orderAction(String userId, String gameId, Order order);

}
