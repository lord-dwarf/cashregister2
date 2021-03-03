package com.polinakulyk.cashregister2.config;

import com.polinakulyk.cashregister2.util.Util;

import static com.polinakulyk.cashregister2.util.Util.getPropertyNotBlank;

/**
 * Holder of the application configuration.
 */
public final class Config {

    private static class Singleton {
        public static final Config INSTANCE = new Config();
    }

    // DB
    private final String datasourceUrl;
    private final String datasourceUsername;
    private final String datasourcePassword;

    private final Integer dbpoolSize;
    private final Integer dbpoolTimeout;

    // Auth
    private final String cashregisterAuthSalt;


    private Config() {
        var properties = Util.getProperties();

        // Datasource
        datasourceUrl = getPropertyNotBlank(properties, "datasource.url");
        datasourceUsername =
                getPropertyNotBlank(properties, "datasource.username");
        datasourcePassword =
                getPropertyNotBlank(properties, "datasource.password");

        // DB connection pool
        dbpoolSize =
                Integer.parseInt(getPropertyNotBlank(properties, "dbpool.size"));
        dbpoolTimeout =
                Integer.parseInt(getPropertyNotBlank(properties, "dbpool.timeout"));

        // Auth
        cashregisterAuthSalt =
                getPropertyNotBlank(properties, "cashregister.auth.salt");
    }

    public static Config getConfig() {
        return Singleton.INSTANCE;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public Integer getDbpoolSize() {
        return dbpoolSize;
    }

    public Integer getDbpoolTimeout() {
        return dbpoolTimeout;
    }

    public String getCashregisterAuthSalt() {
        return cashregisterAuthSalt;
    }
}
