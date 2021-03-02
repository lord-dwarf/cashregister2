package com.polinakulyk.cashregister2.controller.dto;

import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * Represents all available localization languages.
 *
 * Holds the sole locale for a given language.
 */
public enum Language {
    EN(Locale.US),
    UA(new Locale("uk", "UA"));

    private Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * Finds the enum value by a given string.
     *
     * @param s
     * @return
     */
    public static Optional<Language> fromString(String s) {
        return stream(values())
                .filter(lang -> lang.name().equals(s))
                .findFirst();
    }
}
