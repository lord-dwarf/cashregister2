package com.polinakulyk.cashregister2.security;

import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterAuthorizationException;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.polinakulyk.cashregister2.security.dto.UserRole.GUEST;
import static com.polinakulyk.cashregister2.util.Util.getProperties;
import static com.polinakulyk.cashregister2.util.Util.getExistingProperty;
import static com.polinakulyk.cashregister2.util.Util.toBase64;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthHelper {

    private static final String SESSION_AUTHENTICATED_USER = "authenticatedUser";

    private static final String SALT;

    static {
        var properties = getProperties();
        SALT = toBase64(
                getExistingProperty(properties, "cashregister.auth.salt"));
    }

    public String encodePassword(String password) {
        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                SALT.getBytes(UTF_8),
                65536,
                128);
        try {
            var passwordEncoder = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return toBase64(passwordEncoder.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CashRegisterException(e);
        }
    }

    public boolean isUserPasswordMatches(String password, String expectedEncodedPassword) {
        return encodePassword(password).equals(expectedEncodedPassword);
    }

    public void putUserIntoSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        user.setPassword("");
        session.setAttribute(SESSION_AUTHENTICATED_USER, user);
    }

    public void removeUserFromSessionIfNeeded(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(SESSION_AUTHENTICATED_USER, null);
        }
    }

    public Optional<User> getUserFromSession(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
                .map((session) -> (User) session.getAttribute(SESSION_AUTHENTICATED_USER));
    }

    public String getUserIdFromSession(HttpServletRequest request) {
        return getUserFromSession(request)
                .orElseThrow(() -> new CashRegisterAuthorizationException("No user session"))
                .getId();
    }

    public UserRole getUserRoleFromSession(HttpServletRequest request) {
        return getUserFromSession(request)
                .orElseThrow(() -> new CashRegisterAuthorizationException("No user session"))
                .getRole();
    }

    public boolean isAuthorized(HttpServletRequest request, Set<UserRole> roles) {
        var user = getUserFromSession(request);
        var userRole = user.isPresent() ? user.get().getRole() : GUEST;
        return roles.stream().anyMatch((role) -> role == userRole);
    }
}
