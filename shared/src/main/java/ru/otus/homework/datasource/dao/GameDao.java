package ru.otus.homework.datasource.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameDao.class);

    @Autowired
    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addGame(String gameId, List<String> users) {
        String sql = "insert into t_game (id_game, created_date) values (?,now())";
        jdbcTemplate.update(sql, gameId);

        users.forEach((user) -> {
            String sql2 = """
                        insert into t_active_game (id_game, id_user, created_date)
                        select (select id from t_game where id_game = ?), u.id, now()
                        from t_user u
                        where u.username = ?
                    """;
            jdbcTemplate.update(sql2, gameId, user);
        });

        LOGGER.info("Added new game {}", gameId);
    }

    public List<String> getUsersByGameId(String gameId) {
        String sql = """
                select u.username
                from t_user u
                join t_active_game ag ON ag.id_user = u.id
                join t_game g on g.id = ag.id_game
                where g.id_game = ?
                """;
        return jdbcTemplate.queryForList(sql, String.class, gameId);
    }
}
