package com.polinakulyk.cashregister2.service;

import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.repository.UserRepository;
import com.polinakulyk.cashregister2.exception.CashRegisterAuthorizationException;
import com.polinakulyk.cashregister2.exception.CashRegisterUserNotFoundException;
import com.polinakulyk.cashregister2.security.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository = new UserRepository();
    private final AuthHelper authHelper = new AuthHelper();

    public User login(String login, String password) {
        log.debug("BEGIN Login of user '{}'", login);

        User user = findExistingByUsername(login);

        // authenticate user
        if (!authHelper.isUserPasswordMatches(password, user.getPassword())) {
            throw new CashRegisterAuthorizationException(login);
        }

        log.info("DONE Login of user '{}' with role '{}'", login, user.getRole());
        return user;
    }

    public User findExistingById(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() ->
                new CashRegisterUserNotFoundException(userId));

        log.debug("DONE Find existing user by id: '{}'", user.getId());
        return user;
    }

    public User findExistingByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new CashRegisterUserNotFoundException(username));

        log.debug("DONE Find existing user by username: '{}', user: '{}'", username, user.getId());
        return user;
    }
}
