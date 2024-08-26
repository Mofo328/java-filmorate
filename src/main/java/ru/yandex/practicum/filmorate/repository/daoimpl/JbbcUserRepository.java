package ru.yandex.practicum.filmorate.repository.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.dao.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
public class JbbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JbbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User addUser(User userRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO users (user_name,login,email,birthday) VALUES" +
                "(:user_name,:login,:email,:birthday)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_name", userRequest.getName())
                .addValue("login", userRequest.getLogin())
                .addValue("email", userRequest.getEmail())
                .addValue("birthday", userRequest.getBirthday());
        jdbcTemplate.update(sql, parameterSource, keyHolder);
        userRequest.setId(keyHolder.getKey().longValue());
        return userRequest;
    }

    @Override
    public User updateUser(User userRequest) {
        final String sql = "UPDATE users SET user_name =:user_name,login =:login," +
                "email=:email,birthday=:birthday WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userRequest.getId())
                .addValue("user_name", userRequest.getName())
                .addValue("login", userRequest.getLogin())
                .addValue("email", userRequest.getEmail())
                .addValue("birthday", userRequest.getBirthday());
        jdbcTemplate.update(sql, parameterSource);
        return userRequest;
    }

    @Override
    public boolean userDelete(Long id) {
        final String sql = "DELETE FROM users WHERE user_id = user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", id);
        return jdbcTemplate.update(sql, parameterSource) > 0;
    }

    @Override
    public Collection<User> allUsers() {
        final String sql = "SELECT * FROM users";
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        return jdbcTemplate.query(sql, parameterSource,
                (rs, rowNum) -> createUserFromResultSet(rs));
    }

    public Optional<User> userGet(Long id) {
        final String sql = "SELECT * FROM users WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameterSource,
                (rs, rowNum) -> createUserFromResultSet(rs)));
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("user_name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}
