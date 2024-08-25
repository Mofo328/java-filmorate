package ru.yandex.practicum.filmorate.repository.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.dao.GenreRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;

    @Autowired
    public JdbcGenreRepository(NamedParameterJdbcOperations jdbc) {
        this.jdbcTemplate = jdbc;
    }

    @Override
    public Optional<Genre> genreById(Long id) {
        final String sql = "SELECT genre_id, genre_name AS name FROM genres WHERE genre_id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, parameterSource, (rs, rowNum) ->
                    new Genre(rs.getLong("genre_id"), rs.getString("genre_name"))
            );
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
    }


}
