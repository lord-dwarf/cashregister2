package com.polinakulyk.cashregister2.security.dto;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.util.Optional;
import java.util.Set;

import static com.polinakulyk.cashregister2.util.Util.quote;
import static java.util.Arrays.stream;

public enum UserRole {
    MERCH,
    TELLER,
    SR_TELLER,
    GUEST;

    /**
     * Finds the enum value by a given string.
     * <p>
     * Case-insensitive.
     *
     * @param userRoleStr
     * @return
     */
    public static Optional<UserRole> fromString(String userRoleStr) {
        return stream(values())
                .filter((userRole) -> userRole.name().equalsIgnoreCase(userRoleStr))
                .findFirst();
    }

    /**
     * Finds the enum value by a given integer.
     *
     * @param userRoleInt
     * @return
     */
    public static Optional<UserRole> fromInteger(Integer userRoleInt) {
        return stream(values())
                .filter((userRole) -> userRole.ordinal() == userRoleInt)
                .findFirst();
    }

    /**
     * Finds the existing enum value by a given integer, otherwise throws.
     *
     * @param userRoleInt
     * @return
     */
    public static UserRole fromExistingInteger(Integer userRoleInt) {
        return fromInteger(userRoleInt).orElseThrow(() ->
                new CashRegisterException(quote("User role not found", userRoleInt)));
    }

    /**
     * Provides set of roles for routes that allow any authenticated user.
     *
     * @return
     */
    public static Set<UserRole> authenticated() {
        return Set.of(MERCH, TELLER, SR_TELLER);
    }

    /**
     * Provides set of roles for routes that don't require authentication.
     *
     * @return
     */
    public static Set<UserRole> any() {
        return Set.of(values());
    }

    /**
     * Provides teller user roles, for routes that require any authenticated teller.
     *
     * @return
     */
    public static Set<UserRole> tellers() {
        return Set.of(TELLER, SR_TELLER);
    }
}
