package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.db.DbHelper.tryClose;
import static com.polinakulyk.cashregister2.db.DbHelper.tryCommit;
import static com.polinakulyk.cashregister2.db.DbHelper.tryRollback;

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
}
