package ru.yandex.practicum.filmorate;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;

@SpringBootTest
public class FilmControllerTest {


    @Autowired
    private FilmController filmController;

    @Test
    public void testFilmAdd() {
        Film filmToAdd = new Film();
        filmToAdd.setName("Test Film");
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);

        Film addedFilm = filmController.filmAdd(filmToAdd);

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

        Film updatedFilm = filmController.filmUpdate(filmToUpdate);

        assertNotNull(updatedFilm);
        assertEquals(filmToUpdate.getName(), updatedFilm.getName());
    }

    @Test
    public void testAllFilms() {
        Collection<Film> films = filmController.allFilms();
        assertNotNull(films);
        Assert.assertTrue(films.isEmpty());
    }

    @Test
    public void testNameCorrect() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmController.filmAdd(filmToAdd));
    }

    @Test
    public void testDescriptionCorrect() {
        Film filmToAdd = new Film();
        String description = "Hello world ";
        while (description.length() < 200) {
            description += "Hello world ";
        }
        filmToAdd.setDescription(description.substring(0, 200));
        filmToAdd.setReleaseDate(LocalDate.now());
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmController.filmAdd(filmToAdd));
    }

    @Test
    public void testDateOfReleaseIsCorrect() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.of(1894, 12, 28));
        filmToAdd.setDuration(120);
        assertThrows(ValidationException.class, () -> filmController.filmAdd(filmToAdd));
    }

    @Test
    public void testDurationMustBePositiveNumber() {
        Film filmToAdd = new Film();
        filmToAdd.setDescription("Test Description");
        filmToAdd.setReleaseDate(LocalDate.of(1894, 12, 28));
        filmToAdd.setDuration(-100);
        assertThrows(ValidationException.class, () -> filmController.filmAdd(filmToAdd));
    }
}