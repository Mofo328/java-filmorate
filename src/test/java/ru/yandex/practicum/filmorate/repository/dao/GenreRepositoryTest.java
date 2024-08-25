package ru.yandex.practicum.filmorate.repository.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreRepositoryTest {

    @Autowired
    GenreRepository genreRepository;

    @Test
    void shouldGetAllGenresTest() {
        Optional<List<Genre>> genresOptional = Optional.ofNullable(genreRepository.getAllGenre());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres -> {
                            assertThat(genres).isNotEmpty();
                            assertThat(genres).hasSize(6);
                            assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genres).element(0)
                                    .hasFieldOrPropertyWithValue("name", "Комедия");
                            assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(genres).element(1)
                                    .hasFieldOrPropertyWithValue("name", "Драма");
                            assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(genres).element(2)
                                    .hasFieldOrPropertyWithValue("name", "Мультфильм");
                            assertThat(genres).element(3).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(genres).element(3)
                                    .hasFieldOrPropertyWithValue("name", "Триллер");
                            assertThat(genres).element(4).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(genres).element(4)
                                    .hasFieldOrPropertyWithValue("name", "Документальный");
                            assertThat(genres).element(5).hasFieldOrPropertyWithValue("id", 6L);
                            assertThat(genres).element(5)
                                    .hasFieldOrPropertyWithValue("name", "Боевик");
                        }
                );
    }

    @Test
    void shouldGetGenreByIdTest() {
        Optional<Genre> genreOp = genreRepository.genreById(1L);
        assertThat(genreOp)
                .isPresent()
                .hasValueSatisfying(genres -> {
                    assertThat(genres).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(genres).hasFieldOrPropertyWithValue("name", "Комедия");
                });
    }
}