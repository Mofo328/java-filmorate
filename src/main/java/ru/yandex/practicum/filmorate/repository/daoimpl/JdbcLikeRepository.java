package ru.yandex.practicum.filmorate.repository.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.dao.LikeRepository;

@Repository
public class JdbcLikeRepository implements LikeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLikeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        final String sql = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        return jdbcTemplate.update(sql, parameterSource) > 0;
    }

    @Override
    public boolean likeRemove(Long filmId, Long userId) {
        final String sql = "DELETE FROM likes WHERE film_id = film_id AND user_id = user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        return jdbcTemplate.update(sql, parameterSource) > 0;
    }
}

