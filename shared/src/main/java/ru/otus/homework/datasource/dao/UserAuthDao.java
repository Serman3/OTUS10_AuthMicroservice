package ru.otus.homework.datasource.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.Token;
import ru.otus.homework.TokenUser;
import ru.otus.homework.datasource.dto.UserDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserAuthDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserAuthDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsDeactivateToken(Token token) {
        return this.jdbcTemplate.queryForObject("select exists(select id from t_deactivated_token where id = ?)", Boolean.class, token.id());
    }

    @Transactional
    public void addUser(UserDto userDto) {
        this.jdbcTemplate.update("insert into t_user (username, password) values (?, ?)", userDto.getUsername(), "{noop}" + userDto.getPassword());
        addRoleByUsername(userDto.getUsername(), "ROLE_USER");
    }

    public void addRoleByUsername(String username, String role) {
        Optional<UserDto> userDtoOptional = findUserByUsername(username);
        userDtoOptional.ifPresent(userDto -> {
            this.jdbcTemplate.update("insert into t_user_authority(id_user, c_authority) values (?, ?)", userDto.getId(), role);
        });
    }

    public void addDeactivateToken(TokenUser user) {
        this.jdbcTemplate.update("insert into t_deactivated_token (id, c_keep_until) values (?, ?)", user.getToken().id(), Date.from(user.getToken().expiresAt()));
    }

    public Optional<UserDto> findUserByUsername(String username) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("select * from t_user where username = ?", new BeanPropertyRowMapper<>(UserDto.class), username));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<String> findAllUserRolesByUserId(Long id) {
        return jdbcTemplate.queryForList("select c_authority from t_user_authority where id_user = ?", String.class, id);
    }

    public List<String> findAllUserRolesByUserName(String username) {
        return jdbcTemplate.queryForList("""
                select c_authority
                from t_user_authority ua
                join t_user u on u.id = ua.id_user
                where u.username = ?
                """, String.class, username);
    }

}
