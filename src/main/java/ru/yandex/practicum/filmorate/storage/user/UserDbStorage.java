package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                null)
        );
    }

    @Override
    public User createUser(User user) throws ValidationException {
        if (!validate(user)) {
            throw new ValidationException();
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {

        if (getUserById(user.getId()) != null) {
            String sqlQuery = "UPDATE USERS SET " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Пользователь с ID={} успешно обновлен", user.getId());
            return user;
        } else {
            throw new NotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public User getUserById(Integer userId) {
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE id = ?", userId);
        if (userRows.first()) {
            user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate(),
                    null);
        } else {
            throw new NotFoundException("Пользователь не найден!");
        }
        return user;
    }

    @Override
    public User deleteUser(Integer userId) {
        User user = getUserById(userId);
        String sqlQuery = "DELETE FROM USERS WHERE id = ? ";
        if (jdbcTemplate.update(sqlQuery, userId) == 0) {
            throw new NotFoundException("Пользователь с ID=" + userId + " не найден!");
        }
        return user;
    }
    private boolean validate(User user) throws ValidationException {
        boolean validationResult = false;
        try {
            String userName = user.getName();
            String userEmail = user.getEmail();
            String userLogin = user.getLogin();
            if (!userEmail.contains("@")) {
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
            } else if (userLogin.contains(" ") || userLogin.equals("")) {
                throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения пользователя не может быть в будущем.");
            } else {
                validationResult = true;
                if (userName == null || userName.equals("")) {
                    user.setName(user.getLogin());
                }
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return validationResult;
    }
}