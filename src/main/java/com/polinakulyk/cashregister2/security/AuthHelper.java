package com.polinakulyk.cashregister2.security;

import com.polinakulyk.cashregister2.config.Config;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterAuthorizationException;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.db.dto.UserRole;
import com.polinakulyk.cashregister2.util.Util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.polinakulyk.cashregister2.config.Config.getConfig;
import static com.polinakulyk.cashregister2.db.dto.UserRole.GUEST;
import static com.polinakulyk.cashregister2.util.Util.getProperties;
import static com.polinakulyk.cashregister2.util.Util.getPropertyNotBlank;
import static com.polinakulyk.cashregister2.util.Util.toBase64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

/**
 * Helper utility class for authentication, authorization and security in general.
 */
public final class AuthHelper {

    private static final String SESSION_AUTHENTICATED_USER_ATTR = "authenticatedUser";

    private AuthHelper() {
        throw new UnsupportedOperationException("Can not instantiate");
    }

    /**
     * Gets standard Java KDF encoder for password encoding, with a reasonably secure complexity
     * of execution (iteration count params), and encodes password + salt.
     *
     * @param password
     * @return
     */
    public static String encodePassword(String password) {
        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                // get salt from config
                toBase64(getConfig().getCashregisterAuthSalt()).getBytes(UTF_8),
                65536,
                128);
        try {
            // get standard Java password encoder
            var passwordEncoder = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return toBase64(passwordEncoder.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CashRegisterException(e);
        }
    }

    /**
     * Determines whether the given password matches a KDF-encoded password.
     *
     * @param password
     * @param expectedEncodedPassword
     * @return
     */
    public static boolean isUserPasswordMatches(String password, String expectedEncodedPassword) {
        return encodePassword(password).equals(expectedEncodedPassword);
    }

    /**
     * Either gets an existing session or creates a new one and then puts user into it.
     *
     * @param user
     * @param request
     */
    public static void putUserIntoSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        // do not keep user password in cleartext
        user.setPassword("");
        session.setAttribute(SESSION_AUTHENTICATED_USER_ATTR, user);
    }

    /**
     * For an existing session clean up the authenticated user.
     *
     * @param request
     */
    public static void removeUserFromSessionIfNeeded(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(SESSION_AUTHENTICATED_USER_ATTR, null);
        }
    }

    /**
     * Returns authenticated user from existing session.
     *
     * @param request
     * @return
     */
    public static Optional<User> getUserFromSession(HttpServletRequest request) {
        return ofNullable(request.getSession(false))
                .map(session -> (User) session.getAttribute(SESSION_AUTHENTICATED_USER_ATTR));
    }

    /**
     * Returns the authenticated user id from existing session, otherwise throws auth exception.
     *
     * @param request
     * @return user id
     */
    public static String getUserIdFromSession(HttpServletRequest request) {
        return getUserFromSession(request)
                .orElseThrow(() -> new CashRegisterAuthorizationException("No user session"))
                .getId();
    }

    /**
     * Returns the authenticated user role from existing session, otherwise throws auth exception.
     *
     * @param request
     * @return user role
     */
    public static UserRole getUserRoleFromSession(HttpServletRequest request) {
        return getUserFromSession(request)
                .orElseThrow(() -> new CashRegisterAuthorizationException("No user session"))
                .getRole();
    }

    /**
     * Determines if user in session is authorized against the provided set of roles.
     * If user session does not exist, the user is assumed to have a GUEST role.
     *
     * @param request
     * @param roles
     * @return is user authorized
     */
    public static boolean isAuthorized(HttpServletRequest request, Set<UserRole> roles) {
        var user = getUserFromSession(request);
        var userRole = user.isPresent() ? user.get().getRole() : GUEST;
        return roles.stream().anyMatch(role -> role == userRole);
    }
}
