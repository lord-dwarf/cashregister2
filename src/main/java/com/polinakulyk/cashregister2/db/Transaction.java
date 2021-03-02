package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable {

    private static final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

    private final boolean isNestedTransaction;

    private Transaction(boolean isNestedTransaction) {
        this.isNestedTransaction = isNestedTransaction;
    }

    public static Transaction beginTransaction() {
        if (transactionConnection.get() != null) {
            return new Transaction(true);
        }
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new CashRegisterException("Could not get transaction connection", e);
        }
        transactionConnection.set(conn);
        return new Transaction(false);
    }

    public static Connection getTransactionalConnection() throws SQLException {
        Connection conn = transactionConnection.get();
        if (conn == null) {
            throw new CashRegisterException("getConnection() must happen after beginTransaction()");
        }
        return conn;
    }

    public static void rollbackIfNeeded(Connection conn, boolean succ) {
        if (!succ && conn != null) {
            tryRollback(conn);
        }
    }

    public void commitIfNeeded() {
        if (isNestedTransaction) {
            return;
        }
        Connection conn = transactionConnection.get();
        tryCommit(conn);
        tryClose(conn);
    }

    @Override
    public void close() {
        if (isNestedTransaction) {
            return;
        }
        Connection conn = transactionConnection.get();
        try {
            tryRollback(conn);
            tryClose(conn);
        } finally {
            transactionConnection.remove();
        }
    }

    /**
     *
     * @param conn
     */
    public static void tryCommit(Connection conn) {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new CashRegisterException("Could not commit", e);
        }
    }

    public static void tryRollback(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not rollback", e);
        }
    }

    public static void tryClose(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not close connection", e);
        }
    }
}
