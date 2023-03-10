import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class FilmUserValidationTesting {
    FilmStorage filmStorage  = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage);
    FilmController filmController = new FilmController(filmService);

    UserStorage userStorage = new InMemoryUserStorage();

    UserService userService = new UserService(userStorage);

    UserController userController = new UserController(userService);


    @Test
    public void simpleAddFilm() throws ValidationException {
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(2020, 8, 20),
                150);

        filmController.addFilm(film);
        List<Film> filmArr = filmController.getAllFilms();
        assertEquals(filmArr.size(), 1);


    }

    @Test
    public void simpleUpdateFilm() throws ValidationException {
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(2020, 8, 20),
                140);

        filmController.addFilm(film);

        Film filmUpdate = new Film("Tenet", "K.Nolan film", LocalDate.of(2020, 8, 20),
                150);

        filmController.updateFilm(filmUpdate);

        List<Film> filmArr = filmController.getAllFilms();
        Film afterUpdate = filmArr.get(0);

        assertEquals(afterUpdate.getDuration(), 150);

    }

    @Test
    public void emptyFilmName(){
        Film film = new Film("", "K.Nolan film", LocalDate.of(2020, 8, 20),
                150);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("???????????????? ???????????? ???? ?????????? ???????? ????????????.", exception.getMessage());

    }

    @Test
    public void filmDescriptionMoreThan200Symbols(){
        Film film = new Film("Tenet", "qweertyuosdkdsfksjcbskcjbsdkcjbsdkcjsbdkcjsbdcjksbckjsveeeeee" +
                "ghgbfjkhbdfkjbkfjwebfwobwoebdowburbfwrioubfowfbrwufbworufbworubfowdbhfworefbwroufbubvqp[rurbqfiffff" +
                "djfhvbdfjhvbdfjhvbdfbvjdfhvbdjfhvbdjkfhbvslfkhjvbslkdfbvwpeivubfdvkjbdfvkdvbdflkvksbdfkvbdveivbfkvb" +
                "dkfjvbdfkkkkvvvvvvdkkerooooooooooooooooowwwwwwwwwwwwwwweooooooooooooooooooorrrrrrrrrrrrrrrrooooooof" +
                "dkfjdnvvvvvvvvvvvvvvvvvfjdddddddddddfkeeeeeeeeeeeeeeeeeeurfffffffffffffffffffffffffffffffffffffffbf" +
                "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "", LocalDate.of(2020, 8, 20),
                150);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("???????????????????????? ?????????? ???????????????? ???????????? ??? 200 ????????????????.", exception.getMessage());

    }
    @Test
    public void filmBadReleaseDate(){
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(1812, 9, 7),
                150);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("???????? ???????????? ???????????? ???? ?????????? ???????? ?????????? 28 ?????????????? 1895??.", exception.getMessage());

    }

    @Test
    public void filmBadDuration(){
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(2020, 8, 20),
                -1);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("?????????????????????????????????? ???????????? ???????????? ???????? ??????????????????????????.", exception.getMessage());
    }

    @Test
    public void simpleAddUser() throws ValidationException {
        User user = new User ("k.nolan@yandex.ru", "dunkirk", "Christopher",
                LocalDate.of(1970, 7, 30));

            userController.addUser(user);

            List<User> userArr = userController.getAllUsers();
            assertEquals(userArr.size(), 1);

    }

    @Test
    public void simpleUpdateUser() throws ValidationException {
        User user = new User ("k.nolan@yandex.ru", "nolan", "Christopher",
                LocalDate.of(1970, 7, 30));

        userController.addUser(user);

        User userUpdate = new User ("k.nolan@yandex.ru", "dunkirk", "Christopher",
                LocalDate.of(1970, 7, 30));

        userController.updateUser(userUpdate);

        List<User> userArr = userController.getAllUsers();
        User afterUpdate = userArr.get(0);

        assertEquals(afterUpdate.getLogin(), "dunkirk");

    }

    @Test
    public void userBadEmail(){
        User user = new User ("", "dunkirk", "Christopher",
                LocalDate.of(1970, 7, 30));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("?????????????????????? ?????????? ???? ?????????? ???????? ???????????? ?? ???????????? ?????????????????? ???????????? @.", exception.getMessage());

    }

    @Test
    public void userBadLogin(){
        User user = new User ("k.nolan@yandex.ru", "", "Christopher",
                LocalDate.of(1970, 7, 30));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("?????????? ???? ?????????? ???????? ???????????? ?? ?????????????????? ??????????????.", exception.getMessage());

    }

    @Test
    public void userBadBirthday(){
        User user = new User ("k.nolan@yandex.ru", "dunkirk", "Christopher",
                LocalDate.of(2024, 7, 30));


        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("???????? ???????????????? ???????????????????????? ???? ?????????? ???????? ?? ??????????????.", exception.getMessage());

    }

    @Test
    public void userNoName() throws ValidationException {
        User user = new User ("k.nolan@yandex.ru", "dunkirk", "",
                LocalDate.of(1970, 7, 30));

        userController.addUser(user);

         List<User> userArr  = userController.getAllUsers();

         assertEquals(userArr.get(0).getName(), "dunkirk");



    }

}
