package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    @Test
    public void UserCreationAndGetByIdTest() throws ValidationException {
        Set<Integer> friends = new HashSet<>();

        User nolan = new User(1,"nolan@mail.ru", "nolan1", "Nolan",
                LocalDate.of(1980, 3, 8), friends);

        nolan = userStorage.createUser(nolan);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(nolan.getId()));
        User finalNolan = nolan;
        assertThat(userOptional)
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", finalNolan.getId())
                                .hasFieldOrPropertyWithValue("name", "Nolan"));
    }

    @Test
    public void getAllUsersTest() throws ValidationException {

        Set<Integer> friends = new HashSet<>();
        Set<Integer> friends2 = new HashSet<>();

        User nolan = new User(1,"nolan@mail.ru", "nolan1", "Nolan",
                LocalDate.of(1980, 3, 8), friends);
        User tarantino = new User(2,"tarantino@mail.ru", "tarantino1", "Quentin",
                LocalDate.of(1977, 3, 8), friends2);

        nolan = userStorage.createUser(nolan);
        tarantino = userStorage.createUser(tarantino);
        List<User> usersArr = userStorage.findAllUsers();
        assertThat(usersArr).contains(nolan);
        assertThat(usersArr).contains(tarantino);
    }


    @Test
    public void userDeleteTest() throws ValidationException {
        Set<Integer> friends = new HashSet<>();
        User nolan = new User(1,"nolan@mail.ru", "nolan1", "Nolan",
                LocalDate.of(1980, 3, 8), friends);
        nolan = userStorage.createUser(nolan);
        userStorage.deleteUser(nolan.getId());
        List<User> usersArr = userStorage.findAllUsers();
        assertThat(usersArr).hasSize(0);
    }

    @Test
    public void filmAddingAndFindingById() throws ValidationException {
        Set <Integer> likes  = new HashSet<>();
        Set <Genre> genre = new HashSet<>();
        Mpa mpa = new Mpa(3, "PG-13");
        Film inception = new Film (1, "Inception", "Some description",
                LocalDate.of(2008, 3,8), 180, likes, mpa, genre );
        inception = filmStorage.addFilm(inception);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(inception.getId()));
        Film finalInception = inception;
        assertThat(filmOptional)
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", finalInception.getId())
                        .hasFieldOrPropertyWithValue("name", "Inception")
                );
    }

    @Test
    public void testGetFilms() throws ValidationException {

        Set <Integer> likes1  = new HashSet<>();
        Set <Genre> genre1 = new HashSet<>();
        Mpa mpa1 = new Mpa(3, "PG-13");
        Film inception = new Film (1, "Inception", "Some description",
                LocalDate.of(2008, 3,8), 180, likes1, mpa1, genre1);

        Set <Integer> likes2  = new HashSet<>();
        Set <Genre> genre2 = new HashSet<>();
        Mpa mpa2 = new Mpa(3, "PG-13");
        Film batman = new Film (2, "Batman", "Some description",
                LocalDate.of(2003, 3,8), 180, likes2, mpa2, genre2);

        inception = filmStorage.addFilm(inception);
        batman = filmStorage.addFilm(batman);
        List<Film> filmsArr = filmStorage.getAllFilms();
        assertEquals(2, filmsArr.size());

    }



    @Test
    public void deleteFilm() throws ValidationException {

        Set <Integer> likes1  = new HashSet<>();
        Set <Genre> genre1 = new HashSet<>();
        Mpa mpa1 = new Mpa(3, "PG-13");
        Film inception = new Film (1, "Inception", "Some description",
                LocalDate.of(2008, 3,8), 180, likes1, mpa1, genre1);


        inception = filmStorage.addFilm(inception);

        filmStorage.deleteFilm(inception.getId());
        List<Film> filmsArr = filmStorage.getAllFilms();
        assertThat(filmsArr).hasSize(0);
    }
    @Test
    public void testUpdateFilm() throws ValidationException {
        Set <Integer> likes1  = new HashSet<>();
        Set <Genre> genre1 = new HashSet<>();
        Mpa mpa1 = new Mpa(3, "PG-13");
        Film inception = new Film (1, "Inception", "Some description",
                LocalDate.of(2008, 3,8), 180, likes1, mpa1, genre1);


        inception = filmStorage.addFilm(inception);


        Film inceptionUpdate = new Film (4, "Inception2", "Some other description",
                LocalDate.of(2008, 3,8), 180, likes1, mpa1, genre1);

        Optional<Film> updatedFilmTest = Optional.ofNullable(filmStorage.updateFilm(inceptionUpdate));
        assertThat(updatedFilmTest)
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("name", "Inception2")
                                .hasFieldOrPropertyWithValue("description", "Some other description")
                );
    }


}
