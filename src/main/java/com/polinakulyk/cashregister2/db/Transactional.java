package com.polinakulyk.cashregister2.db;

import com.polinakulyk.cashregister2.exception.CashRegisterException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides functionality for creating and destroying of transactional context that is necessary
 * to supply the JDBC connections to DAO layer, and at the same time control transaction bounds
 * on a Service layer.
 * <p>
 * Supports nested transactional contexts (in a case when outer service calls inner service,
 * and both services are called by a controller layer).
 * E.g. {@link com.polinakulyk.cashregister2.service.ReportService}.
 * <p>
 * EXAMPLE:
 * try (var outer = beginOrContinueTransaction()) { // <- Service layer
 *     // ...
 *     try (var inner = beginOrContinueTransaction()) { // <- Service layer (nested transaction)
 *         // ...
 *         try { // <- DAO layer
 *             var conn = getTransactionalConnection(); // <- connection must stay open here
 *         }
 *         // ...
 *         inner.commitIfNeeded();
 *     } // <- inner.close()
 *     // ...
 *     outer.commitIfNeeded();
 * } // <- outer.close()
 */
public class Transactional implements AutoCloseable {

    // holder for Connection object which is bound to the currently executing thread
    private static final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

    /*
     * To determine whether this Transactional instance should continue outer transaction.
     *
     * EXAMPLE:
     * try (var outer = beginOrContinueTransaction()) {
     *     // ...
     *     try (var inner = beginOrContinueTransaction()) {
     *         // ...
     *         inner.commitIfNeeded();
     *     } // <- inner.close()
     *     // ...
     *     outer.commitIfNeeded();
     * } // <- outer.close()
     *
     */
    private final boolean isNested;

    private Transactional(boolean isNested) {
        this.isNested = isNested;
    }

    /**
     * Either obtain a new connection from underlying connection pool
     * or reuse already existing connection from {@link ThreadLocal},
     * for the case of nested {@link Transactional}.
     *
     * @return connection
     */
    public static Transactional beginOrContinueTransaction() {
        if (transactionConnection.get() != null) {
            return new Transactional(true);
        }
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new CashRegisterException("Could not get transactional connection", e);
        }
        transactionConnection.set(conn);
        return new Transactional(false);
    }

    /**
     * Either obtain a connection with active transaction from {@link ThreadLocal}
     * or throw.
     *
     * @return connection
     */
    public static Connection getTransactionalConnection() {
        Connection conn = transactionConnection.get();
        if (conn == null) {
            throw new CashRegisterException(
                    "getTransactionalConnection() must happen after beginOrContinueTransaction()");
        }
        return conn;
    }

    /**
     * Either commit and close the connection with active transaction or do nothing
     * (in a case of nested {@link Transactional}).
     */
    public void commitIfNeeded() {
        if (isNested) {
            return;
        }
        Connection conn = transactionConnection.get();
        try {
            tryCommit(conn);
        } finally {
            tryClose(conn);
        }
    }

    /**
     * Either rollback and close the connection with active transaction and clean
     * {@link ThreadLocal} or do nothing (in a case of nested {@link Transactional}).
     */
    @Override
    public void close() {
        if (isNested) {
            return;
        }
        Connection conn = transactionConnection.get();
        try {
            tryRollback(conn);
        } finally {
            tryClose(conn);
            // clean up of ThreadLocal connection holder
            transactionConnection.remove();
        }
    }

    /*
     * Do commit in a given connection, get rid of checked exception.
     */
    private static void tryCommit(Connection conn) {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new CashRegisterException("Could not commit", e);
        }
    }

    /*
     * Do rollback in a given connection, get rid of checked exception.
     */
    private static void tryRollback(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not rollback", e);
        }
    }

    /*
     * Close a given connection, get rid of checked exception.
     */
    private static void tryClose(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new CashRegisterException("Could not close connection", e);
        }
    }
}
