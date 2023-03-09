package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private MpaStorage mpaStorage;
    private LikeStorage likeStorage;

    private GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage,
                         LikeStorage likeStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
    }


    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, fm.user_id, fm.film_id " +
                "FROM FILMS as f LEFT JOIN FILM_LIKES as fm ON f.id = fm.film_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(List.of(rs.getInt("user_id"))),
                mpaStorage.getMpaById(rs.getInt("rating_id")),
                new HashSet<>(genreStorage.getFilmGenres(rs.getInt("id"))))
        );

    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (!validate(film)) {
            throw new ValidationException();
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreStorage.getGenreById(genre.getId()).getName());
            }
            genreStorage.putGenres(film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (!validate(film)) {
            throw new ValidationException();
        }
        String sqlQuery = "UPDATE FILMS SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
                for (Genre genre : film.getGenres()) {
                    genre.setName(genreStorage.getGenreById(genre.getId()).getName());
                }
            }
            genreStorage.putGenres(film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
    }

    @Override
    public Film getFilmById(Integer filmId) {
        Film film;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "f.rating_id, fm.user_id, fm.film_id " +
                "FROM FILMS as f " +
                "LEFT JOIN FILM_LIKES as fm ON f.id = fm.film_id " +
                " WHERE id = ?", filmId);
        if (filmRows.first()) {
            Mpa mpa = mpaStorage.getMpaById(filmRows.getInt("rating_id"));
            Set<Genre> genres = new HashSet<>(genreStorage.getFilmGenres(filmId));
            film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getInt("duration"),
                    new HashSet<>(List.of(filmRows.getInt("user_id"))),
                    mpa,
                    genres);
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
        return film;
    }

    @Override
    public Film deleteFilm(Integer filmId) {
        Film film = getFilmById(filmId);
        String sqlQuery = "DELETE FROM FILMS WHERE id = ? ";
        if (jdbcTemplate.update(sqlQuery, filmId) == 0) {
            throw new NotFoundException("Фильм не найден!");
        }
        return film;
    }

    private boolean validate(Film film) throws ru.yandex.practicum.filmorate.exceptions.ValidationException {
        boolean validationResult = false;
        try {
            String filmName = film.getName();
            if (filmName.isEmpty()) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Название фильма не может быть пустым.");
            } else if (film.getDescription().length() > 200) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Максимальная длина описания фильма — 200 символов.");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895г.");
            } else if (film.getDuration() < 0) {
                throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Продолжительность фильма должна быть положительной.");
            } else {
                validationResult = true;
            }
        } catch (ru.yandex.practicum.filmorate.exceptions.ValidationException exception) {
            throw new ru.yandex.practicum.filmorate.exceptions.ValidationException(exception.getMessage());
        }
        return validationResult;
    }

}