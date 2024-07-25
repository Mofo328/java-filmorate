package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;

@SpringBootTest
public class FilmControllerTest {


    @Autowired
    private FilmStorage filmStorage;

    @Test
    public void testFilmAdd() {
        Film filmToAdd = new Film();
        filmToAdd.setName("Test Film");
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);

        Film addedFilm = filmStorage.filmAdd(filmToAdd);

        assertNotNull(addedFilm);
        assertNotNull(addedFilm.getId());
        assertEquals(filmToAdd.getName(), addedFilm.getName());
        assertEquals(filmToAdd.getDescription(), addedFilm.getDescription());
        assertEquals(filmToAdd.getReleaseDate(), addedFilm.getReleaseDate());
        assertEquals(filmToAdd.getDuration(), addedFilm.getDuration());
    }

    @Test
    public void testFilmUpdate() {
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1L);
        filmToUpdate.setName("Updated Film Name");
        filmToUpdate.setDescription("Test Description");
        filmToUpdate.setReleaseDate(LocalDate.now());
        filmToUpdate.setDuration(120);

        Film updatedFilm = filmStorage.filmUpdate(filmToUpdate);

        assertNotNull(updatedFilm);
        assertEquals(filmToUpdate.getName(), updatedFilm.getName());
    }

    @Test
    public void testAllFilms() {
        Collection<Film> films = filmStorage.allFilms();
        assertNotNull(films);
        assertTrue(films.isEmpty());
    }

    @Test
    public void testInvalidFilmNameAddition() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd));
    }

    @Test
    public void testDescriptionUnCorrect() {
        Film filmToAdd = new Film();
        String description = "Hello world ";
        while (description.length() < 200) {
            description += "Hello world ";
        }
        filmToAdd.setDescription(description.substring(0, 200));
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd));
    }

    @Test
    public void testDateOfReleaseUnCorrect() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.of(1894, 12, 28));
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd));
    }

    @Test
    public void testNegativeDurationNotAllowed() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.of(1894, 12, 28));
        filmToAdd.setDuration(-100);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd));
    }

    @Test
    public void testInvalidFilmInputValues() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription(null);
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd));

        Film filmToAdd2 = new Film();
        filmToAdd2.setDescription("Test Description");
        filmToAdd2.setReleaseDate(null);
        filmToAdd2.setDuration(120);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd2));

        Film filmToAdd3 = new Film();
        filmToAdd3.setDescription("Test Description");
        filmToAdd3.setReleaseDate(LocalDate.now());
        filmToAdd3.setDuration(null);
        assertThrows(ValidationException.class, () -> filmStorage.filmAdd(filmToAdd3));
    }
}