package ru.yandex.practicum.filmorate.repository.daoimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.dao.FilmRepository;
import ru.yandex.practicum.filmorate.repository.dao.RatingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcTemplate;
    private final RatingRepository jdbcRatingRepository;

    @Autowired
    public JdbcFilmRepository(NamedParameterJdbcOperations jdbcTemplate, RatingRepository ratingRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcRatingRepository = ratingRepository;
    }

    @Override
    public Film filmAdd(Film filmRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Long ratingId = 0L;
        if (filmRequest.getMpa() != null) {
            ratingId = filmRequest.getMpa().getId();
            checkCorrectMpa(ratingId);
        }
        final String sql = "INSERT INTO films (film_name,description,released,duration,mpa_id) " +
                "VALUES (:film_name,:description,:released,:duration,:mpa_id);";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_name", filmRequest.getName())
                .addValue("description", filmRequest.getDescription())
                .addValue("released", filmRequest.getReleaseDate())
                .addValue("duration", filmRequest.getDuration())
                .addValue("mpa_id", ratingId);

        jdbcTemplate.update(sql, parameterSource, keyHolder);
        filmRequest.setId(keyHolder.getKey().longValue());
        return addExtraFields(filmRequest);
    }

    @Override
    public Film filmUpdate(Film filmRequest) {
        final String sql = "UPDATE FILMS SET film_name = :film_name, description = :description," +
                " released = :released, duration = :duration, mpa_id = :mpa_id WHERE film_id = :film_id;";
        deleteAllFilmGenresById(filmRequest.getId());
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", filmRequest.getId())
                .addValue("film_name", filmRequest.getName())
                .addValue("description", filmRequest.getDescription())
                .addValue("released", filmRequest.getReleaseDate())
                .addValue("duration", filmRequest.getDuration())
                .addValue("mpa_id", filmRequest.getMpa().getId());
        jdbcTemplate.update(sql, parameterSource);
        return addExtraFields(filmRequest);
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        final String sql = "SELECT f.film_id, f.film_name, f.description, f.released, f.duration, " +
                "m.mpa_id, m.mpa_name " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = :film_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", id);
        Film film = jdbcTemplate.queryForObject(sql, parameterSource, (rs, rowNum) ->
                createFilmFromResultSet(rs));
        Map<Long, LinkedHashSet<Genre>> filmGenresMap = getAllGenresForFilms();
        film.setGenres(filmGenresMap.getOrDefault(id, new LinkedHashSet<>()));
        return Optional.ofNullable(film);
    }

    @Override
    public boolean filmDelete(Long id) {
        final String sql = "DELETE FROM films WHERE film_id = :film_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", id);
        deleteAllFilmGenresById(id);
        return jdbcTemplate.update(sql, parameterSource) > 0;

    }

    @Override
    public Collection<Film> allFilms() {
        final String sql = "SELECT f.film_id, f.film_name, f.description, f.released, f.duration, " +
                "m.mpa_id, m.mpa_name " +
                " FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id ";
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        Collection<Film> films = jdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                createFilmFromResultSet(rs));
        Map<Long, LinkedHashSet<Genre>> filmGenresMap = getAllGenresForFilms();
        for (Film film : films) {
            film.setGenres(filmGenresMap.getOrDefault(film.getId(), new LinkedHashSet<>()));
        }
        return films;
    }

    @Override
    public Collection<Film> popularFilms(Long count) {
        final String sql = "SELECT f.film_id, f.film_name, f.description, f.released, f.duration, " +
                "m.mpa_id, m.mpa_name, COUNT(l.user_id) AS likes_count " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "GROUP BY f.film_id, f.film_name, f.description, f.released, f.duration, m.mpa_id, m.mpa_name " +
                "ORDER BY likes_count DESC " +
                "LIMIT :count";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("count", count);
        Collection<Film> films = jdbcTemplate.query(sql, parameterSource, (rs, rowNum) ->
                createFilmFromResultSet(rs));
        Map<Long, LinkedHashSet<Genre>> filmGenresMap = getAllGenresForFilms();
        for (Film film : films) {
            film.setGenres(filmGenresMap.getOrDefault(film.getId(), new LinkedHashSet<>()));
        }
        return films;
    }

    private Film addExtraFields(Film film) {
        Long filmId = film.getId();
        Long mpaId = film.getMpa().getId();
        checkCorrectMpa(mpaId);
        if (film.getGenres() != null) {
            List<Long> genreId = film.getGenres().stream().map(Genre::getId).toList();
            filmGenresAdd(filmId, genreId);
        }
        Optional<Rating> filmMpa = jdbcRatingRepository.findRatingById(mpaId);
        LinkedHashSet<Genre> filmGenres = new LinkedHashSet<>(getAllFilmGenresById(filmId));
        film.setMpa(filmMpa.orElseThrow(() -> new ConditionsNotMetException("Рейтинг с ID " + mpaId + " не найден")));
        film.setGenres(filmGenres);
        return film;
    }

    private boolean deleteAllFilmGenresById(Long id) {
        final String sql = "DELETE FROM film_genres WHERE film_id = film_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", id);
        return jdbcTemplate.update(sql, parameterSource) > 0;
    }

    public void filmGenresAdd(Long filmId, List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
        List<SqlParameterSource> batchValues = new ArrayList<>(genreIds.size());
        for (Long genreId : genreIds) {
            checkCorrectGenre(genreId);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("filmId", filmId);
            parameters.addValue("genreId", genreId);
            batchValues.add(parameters);
        }
        jdbcTemplate.batchUpdate(sql, batchValues.toArray(new SqlParameterSource[genreIds.size()]));
    }

    private List<Genre> getAllFilmGenresById(Long id) {
        final String sql = "SELECT g.genre_id, g.genre_name " +
                "FROM film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = :id";

        SqlParameterSource genreParameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(sql, genreParameters, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        });
    }

    private Film createFilmFromResultSet(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("released").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Rating rating = new Rating();
        rating.setId(rs.getLong("mpa_id"));
        rating.setName(rs.getString("mpa_name"));
        film.setMpa(rating);
        return film;
    }

    private void checkCorrectGenre(Long id) {
        final String sql = "SELECT genre_id, genre_name AS name FROM genres WHERE genre_id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sql, parameterSource, (rs, rowNum) ->
                    new Genre(rs.getLong("genre_id"), rs.getString("name"))
            );
        } catch (EmptyResultDataAccessException ignored) {
            throw new ValidationException("Жанр не найден для заданного ID: " + id);
        }
        if (genre.getName().isBlank() || genre.getId() == null) {
            throw new ConditionsNotMetException("Неправильно задан жанр");
        }
    }

    private void checkCorrectMpa(Long id) {
        final String sql = "SELECT mpa_id, mpa_name AS name FROM mpa WHERE mpa_id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        Rating rating;
        try {
            rating = jdbcTemplate.queryForObject(sql, parameterSource, (rs, rowNum) ->
                    new Rating(rs.getLong("mpa_id"), rs.getString("mpa_name"))
            );
        } catch (EmptyResultDataAccessException ignored) {
            throw new ValidationException("Mpa не найден для заданного ID: " + id);
        }
        if (rating.getName().isBlank() || rating.getId() == null) {
            throw new ConditionsNotMetException("Неправильно задан рейтинг");
        }
    }

    private Map<Long, LinkedHashSet<Genre>> getAllGenresForFilms() {
        final String sql = "SELECT fg.film_id, g.genre_id, g.genre_name " +
                "FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.genre_id";

        return jdbcTemplate.query(sql, rs -> {
            Map<Long, LinkedHashSet<Genre>> genresMap = new LinkedHashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                Genre genre = new Genre(genreId, genreName);
                if (!genresMap.containsKey(filmId)) {
                    genresMap.put(filmId, new LinkedHashSet<>());
                }
                genresMap.get(filmId).add(genre);
            }
            return genresMap;
        });
    }
}


