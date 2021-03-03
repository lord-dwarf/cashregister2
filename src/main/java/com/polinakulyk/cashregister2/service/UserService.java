package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.dao.UserDao;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterAuthorizationException;
import com.polinakulyk.cashregister2.exception.CashRegisterUserNotFoundException;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.polinakulyk.cashregister2.security.AuthHelper.isUserPasswordMatches;

/**
 * User service.
 */
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao = new UserDao();

    /**
     * Authenticates user based on the given credentials: username and password.
     *
     * @param login
     * @param password
     * @return
     */
    public User login(String login, String password) {
        log.debug("BEGIN Login of user '{}'", login);

        User user = findExistingByUsername(login);

        // authenticate user
        if (!isUserPasswordMatches(password, user.getPassword())) {
            throw new CashRegisterAuthorizationException(login);
        }

        log.info("DONE Login of user '{}' with role '{}'", login, user.getRole());
        return user;
    }

    /**
     * Find the existing user by id, otherwise throw {@link CashRegisterUserNotFoundException}.
     * <p>
     * Used when we already know that the user exists, and thus it is highly unlikely that
     * the exception will be thrown. But if it happens, an exception specific to our
     * use case will be thrown and provide the necessary HTTP code, (instead of being a general exception
     * {@link java.util.NoSuchElementException} that is thrown by {@link Optional#get()} and will
     * result in HTTP 500).
     *
     * @param userId
     * @return
     */
    public User findExistingById(String userId) {
        var user = userDao.findById(userId).orElseThrow(() ->
                new CashRegisterUserNotFoundException(userId));

        log.debug("DONE Find existing user by id: '{}'", user.getId());
        return user;
    }

    private User findExistingByUsername(String username) {
        User user = userDao.findByUsername(username).orElseThrow(() ->
                new CashRegisterUserNotFoundException(username));

        log.debug("DONE Find existing user by username: '{}', user: '{}'", username, user.getId());
        return user;
    }
}
