package com.polinakulyk.cashregister2.db.dao;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.Transaction;
import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.dto.ReceiptsStatDto;
import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.ReceiptItem;
import com.polinakulyk.cashregister2.db.mapper.ReceiptMapper;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.ConnectionPool.getConnection;
import static com.polinakulyk.cashregister2.util.Util.generateUuid;
import static com.polinakulyk.cashregister2.util.Util.quote;

public class ReceiptDao {

    private static final String FIND_ALL_RECEIPTS_WITH_PAGINATION_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "ORDER BY r.created_time DESC LIMIT ? OFFSET ?;";

    private static final String COUNT_RECEIPTS_SQL =
            "SELECT COUNT(1) AS count " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id;";

    public static final String FIND_RECEIPTS_BY_TELLER_WITH_PAGINATION_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "WHERE u.id = ? AND c.shift_status = 'ACTIVE' AND " +
                    "r.created_time >= c.shift_status_time " +
                    "ORDER BY r.created_time DESC LIMIT ? OFFSET ?;";

    public static final String COUNT_RECEIPTS_BY_TELLER_SQL =
            "SELECT COUNT(1) as count " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "WHERE u.id = ? AND c.shift_status = 'ACTIVE' AND " +
                    "r.created_time >= c.shift_status_time;";

    private static final String FIND_RECEIPT_BY_ID_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "WHERE r.id = ?;";

    private static final String FIND_RECEIPT_ITEMS_BY_RECEIPT_ID_SQL =
            "SELECT ri.id, ri.name, ri.price, ri.amount_unit, ri.amount, " +
                    "ri.product_id , p.code, p.amount_unit, p.amount_available, " +
                    "p.category, p.details, p.name AS product_name, p.price AS product_price " +
                    "FROM receipt_item AS ri LEFT JOIN product AS p ON ri.product_id = p.id " +
                    "WHERE ri.receipt_id = ?;";

    private static final String INSERT_RECEIPT_SQL =
            "INSERT INTO receipt " +
                    "(id, created_time, checkout_time, status, sum_total, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_RECEIPT_SQL =
            "UPDATE receipt " +
                    "SET created_time = ?, checkout_time = ?, status = ?, " +
                    "sum_total = ?, user_id = ? " +
                    "WHERE id = ?;";

    private static final String INSERT_RECEIPT_ITEM_SQL =
            "INSERT INTO receipt_item " +
                    "(id, amount, amount_unit, name, price, receipt_id, product_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_RECEIPT_ITEM_SQL =
            "UPDATE receipt_item " +
                    "SET amount = ?, amount_unit = ?, name = ?, " +
                    "price = ?, receipt_id = ?, product_id = ? " +
                    "WHERE id = ?;";

    private static final String GET_RECEIPTS_STAT_IN_ACTIVE_SHIFT_SQL =
            "SELECT CASE WHEN SUM(r.sum_total) THEN SUM(r.sum_total) ELSE 0 END AS sum, " +
                    "COUNT(*) AS count " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = ? " +
                    "WHERE c.shift_status = ? " +
                    "AND r.created_time >= c.shift_status_time " +
                    "AND r.status = ?;";

    public List<Receipt> findAllWithPagination(int rowsLimit, int rowsOffset) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(FIND_ALL_RECEIPTS_WITH_PAGINATION_SQL);
            statement.setInt(1, rowsLimit);
            statement.setInt(2, rowsOffset);
            ResultSet resultSet = statement.executeQuery();
            List<Receipt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(ReceiptMapper.getReceiptWithUserAndCashbox(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query receipts with pagination", e);
        }
    }

    public int count() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(COUNT_RECEIPTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new CashRegisterException("Can't count receipts");
            }
            return resultSet.getInt("count");
        } catch (SQLException e) {
            throw new CashRegisterException("Can't count receipts", e);
        }
    }

    public List<Receipt> findByTellerWithPagination(
            String tellerId, int rowsLimit, int rowsOffset) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(FIND_RECEIPTS_BY_TELLER_WITH_PAGINATION_SQL);
            statement.setString(1, tellerId);
            statement.setInt(2, rowsLimit);
            statement.setInt(3, rowsOffset);
            ResultSet resultSet = statement.executeQuery();
            List<Receipt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(ReceiptMapper.getReceiptWithUserAndCashbox(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query receipts by teller with pagination", e);
        }
    }

    public int countByTeller(String tellerId) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(COUNT_RECEIPTS_BY_TELLER_SQL);
            statement.setString(1, tellerId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new CashRegisterException("Can't count receipts by teller");
            }
            return resultSet.getInt("count");
        } catch (SQLException e) {
            throw new CashRegisterException("Can't count receipts by teller", e);
        }
    }

    public Optional<Receipt> findById(String receiptId) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_RECEIPT_BY_ID_SQL);
            statement.setString(1, receiptId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                succ = true;
                return Optional.empty();
            }
            var receipt = ReceiptMapper.getReceiptWithUserAndCashbox(resultSet);
            var receiptItems = findReceiptItemsByReceiptId(conn, receiptId);
            receipt.setReceiptItems(receiptItems);
            var result = Optional.of(receipt);

            succ = true;
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find receipt by id", receiptId), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public Receipt insert(String tellerId, Receipt receipt) {
        receipt.setId(generateUuid());

        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(INSERT_RECEIPT_SQL);
            statement.setString(1, receipt.getId());
            statement.setTimestamp(2, DbHelper.toTimestamp(receipt.getCreatedTime()));
            statement.setTimestamp(3, DbHelper.toTimestampNullable(receipt.getCheckoutTime()));
            statement.setInt(4, receipt.getStatus().ordinal());
            statement.setBigDecimal(5, receipt.getSumTotal());
            statement.setString(6, tellerId);

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't create receipt with id", receipt.getId()));
            }

            succ = true;
            return receipt;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't create receipt with id", receipt.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public Receipt update(String userId, Receipt receipt) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_RECEIPT_SQL);
            statement.setTimestamp(1, DbHelper.toTimestamp(receipt.getCreatedTime()));
            statement.setTimestamp(2, DbHelper.toTimestampNullable(receipt.getCheckoutTime()));
            statement.setInt(3, receipt.getStatus().ordinal());
            statement.setBigDecimal(4, receipt.getSumTotal());
            statement.setString(5, userId);
            statement.setString(6, receipt.getId());

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't update receipt with id", receipt.getId()));
            }

            succ = true;
            return receipt;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update receipt with id", receipt.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public ReceiptItem insertReceiptItem(
            String receiptId, String productId, ReceiptItem receiptItem) {
        receiptItem.setId(generateUuid());

        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(INSERT_RECEIPT_ITEM_SQL);
            statement.setString(1, receiptItem.getId());
            statement.setBigDecimal(2, receiptItem.getAmount());
            statement.setInt(3, receiptItem.getAmountUnit().ordinal());
            statement.setString(4, receiptItem.getName());
            statement.setBigDecimal(5, receiptItem.getPrice());
            statement.setString(6, receiptId);
            statement.setString(7, productId);

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't create receipt item with id", receiptItem.getId()));
            }

            succ = true;
            return receiptItem;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't create receipt item with id", receiptItem.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public ReceiptItem updateReceiptItem(
            String receiptId, String productId, ReceiptItem receiptItem) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_RECEIPT_ITEM_SQL);
            statement.setBigDecimal(1, receiptItem.getAmount());
            statement.setInt(2, receiptItem.getAmountUnit().ordinal());
            statement.setString(3, receiptItem.getName());
            statement.setBigDecimal(4, receiptItem.getPrice());
            statement.setString(5, receiptId);
            statement.setString(6, productId);
            statement.setString(7, receiptItem.getId());

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't update receipt item with id", receiptItem.getId()));
            }

            succ = true;
            return receiptItem;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update receipt item with id", receiptItem.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public ReceiptsStatDto getReceiptsStatInActiveShift(String cashboxId) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement =
                    conn.prepareStatement(GET_RECEIPTS_STAT_IN_ACTIVE_SHIFT_SQL);
            statement.setString(1, cashboxId);
            statement.setInt(2, ShiftStatus.ACTIVE.ordinal());
            statement.setInt(3, ReceiptStatus.COMPLETED.ordinal());
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                throw new CashRegisterException(
                        quote("Can't get receipts stat in active shift", cashboxId));
            }
            var sum = rs.getBigDecimal("sum");
            var count = rs.getInt("count");
            var result = new ReceiptsStatDto(sum, count);

            succ = true;
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't get receipts stat in active shift", cashboxId), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    private List<ReceiptItem> findReceiptItemsByReceiptId(Connection connection, String receiptId) throws SQLException {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(FIND_RECEIPT_ITEMS_BY_RECEIPT_ID_SQL);
            statement.setString(1, receiptId);
            ResultSet resultSet = statement.executeQuery();
            List<ReceiptItem> receiptItems = new ArrayList<>();
            while (resultSet.next()) {
                receiptItems.add(ReceiptMapper.getReceiptItemWithProduct(resultSet));
            }
            return receiptItems;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find receipt items by receipt id", receiptId), e);
        }
    }
}
