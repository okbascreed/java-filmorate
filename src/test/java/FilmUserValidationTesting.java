import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ValidationException.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class FilmUserValidationTesting {
    FilmController filmController = new FilmController();
    UserController userController = new UserController();


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
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());

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
        assertEquals("Максимальная длина описания фильма — 200 символов.", exception.getMessage());

    }
    @Test
    public void filmBadReleaseDate(){
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(1812, 9, 7),
                150);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Дата релиза фильма не может быть ранее 28 декабря 1895г.", exception.getMessage());

    }

    @Test
    public void filmBadDuration(){
        Film film = new Film("Tenet", "K.Nolan film", LocalDate.of(2020, 8, 20),
                -1);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
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
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());

    }

    @Test
    public void userBadLogin(){
        User user = new User ("k.nolan@yandex.ru", "", "Christopher",
                LocalDate.of(1970, 7, 30));

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());

    }

    @Test
    public void userBadBirthday(){
        User user = new User ("k.nolan@yandex.ru", "dunkirk", "Christopher",
                LocalDate.of(2024, 7, 30));


        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Дата рождения пользователя не может быть в будущем.", exception.getMessage());

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
