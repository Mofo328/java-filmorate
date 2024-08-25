package ru.yandex.practicum.filmorate.repository.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.dao.RatingRepository;

import java.util.List;
import java.util.Optional;


@Repository
public class JdbcRatingRepository implements RatingRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public JdbcRatingRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Rating> findRatingById(Long id) {
        final String sql = "SELECT mpa_id, mpa_name AS name FROM mpa WHERE mpa_id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Rating rating = jdbc.queryForObject(sql, parameterSource, (rs, rowNum) ->
                    new Rating(rs.getLong("mpa_id"), rs.getString("mpa_name"))
            );
            return Optional.of(rating);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Rating> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbc.query(sql, (rs, rowNum) -> new Rating(rs.getLong("mpa_id"), rs.getString("mpa_name")));
    }
}
