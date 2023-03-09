package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
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

        String getPopularQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration," +
                " f.rating_id, rm.id as mpa_id," +
                " rm.name as mpa_name, g.name as genre_name, " +
                "g.id as genre_id, fg.genre_id as fg_genre_id, fm.user_id as likes FROM FILMS as f " +
                "LEFT JOIN FILM_LIKES as fm ON f.id = fm.film_id " +
                "LEFT JOIN ratings_mpa as rm ON rm.id = f.rating_id " +
                "LEFT JOIN film_genres as fg on f.id = fg.film_id " +
                "LEFT JOIN genres as g on g.id  = fg.genre_id " +
                "GROUP BY f.id ORDER BY COUNT(fm.user_id) DESC LIMIT ?";


        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_Date").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(List.of(rs.getInt("likes"))),
                        new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                        new HashSet<>(genreStorage.getFilmGenres(rs.getInt("id")))),

                count);
    }

    public List<Integer> getLikes(Integer filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }
}