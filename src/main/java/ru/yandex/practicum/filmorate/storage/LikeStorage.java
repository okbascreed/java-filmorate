package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.HashSet;
import java.util.List;


@Component
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private MpaService mpaService;
    private GenreService genreService;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        String getPopularQuery = "SELECT id, name, description, release_date, duration, rating_id FROM FILMS LEFT JOIN FILM_LIKES ON films.id = film_likes.film_id GROUP BY films.id ORDER BY COUNT(film_likes.user_id) DESC LIMIT ?";

        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_Date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getLikes(rs.getInt("id"))),
                        mpaService.getMpaById(rs.getInt("rating_id")),
                        genreService.getFilmGenres(rs.getInt("id"))),
                count);
    }

    public List<Integer> getLikes(Integer filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }
}