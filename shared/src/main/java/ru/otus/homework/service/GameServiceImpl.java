package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.datasource.dao.GameDao;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameDao spaceBattleDao;

    @Autowired
    public GameServiceImpl(GameDao spaceBattleDao) {
        this.spaceBattleDao = spaceBattleDao;
    }

    @Override
    @Transactional
    public String createGame(List<String> users) {
        String uuid = UUID.randomUUID().toString();
        spaceBattleDao.addGame(uuid, users);
        return uuid;
    }

    @Override
    public List<String> getUsersByGameId(String gameId) {
        return spaceBattleDao.getUsersByGameId(gameId);
    }

}
