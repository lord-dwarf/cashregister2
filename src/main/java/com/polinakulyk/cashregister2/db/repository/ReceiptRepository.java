package com.polinakulyk.cashregister2.db.repository;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.ReceiptItem;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.DbHelper.getConnection;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTimeNullable;
import static com.polinakulyk.cashregister2.util.Util.generateUuid;
import static com.polinakulyk.cashregister2.util.Util.quote;

public class ReceiptRepository {

    private static final String FIND_ALL_RECEIPTS_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id;";

    private static final String FIND_ALL_RECEIPTS_WITH_PAGINATION_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "ORDER BY r.created_time DESC LIMIT ? OFFSET ?;";

    private static final String COUNT_RECEIPTS_SQL =
            "SELECT count(1) AS count " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id;";

    /**
     * WARNING change the predicate in sync:
     * {@link ReceiptRepository#FIND_RECEIPTS_BY_TELLER_WITH_PAGINATION_SQL}
     * {@link ReceiptRepository#COUNT_RECEIPTS_BY_TELLER_SQL}
     * {@link com.polinakulyk.cashregister2.service.ServiceHelper#isReceiptInActiveShift}
     */
    public static final String FIND_RECEIPTS_BY_TELLER_WITH_PAGINATION_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, " +
                    "u.full_name, u.username, u.cashbox_id, " +
                    "c.name, c.shift_status, c.shift_status_time " +
                    "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id " +
                    "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id " +
                    "WHERE u.id = ? AND c.shift_status = 'ACTIVE' AND " +
                    "r.created_time >= c.shift_status_time " +
                    "ORDER BY r.created_time DESC LIMIT ? OFFSET ?;";

    /**
     * WARNING change the predicate in sync:
     * {@link ReceiptRepository#FIND_RECEIPTS_BY_TELLER_WITH_PAGINATION_SQL}
     * {@link ReceiptRepository#COUNT_RECEIPTS_BY_TELLER_SQL}
     * {@link com.polinakulyk.cashregister2.service.ServiceHelper#isReceiptInActiveShift}
     */
    public static final String COUNT_RECEIPTS_BY_TELLER_SQL =
            "SELECT count(1) as count " +
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

    public List<Receipt> findAll() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_RECEIPTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            List<Receipt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getReceiptWithUserAndCashbox(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query all receipts", e);
        }
    }

    public List<Receipt> findAllWithPagination(int rowsLimit, int rowsOffset) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(FIND_ALL_RECEIPTS_WITH_PAGINATION_SQL);
            statement.setInt(1, rowsLimit);
            statement.setInt(2, rowsOffset);
            ResultSet resultSet = statement.executeQuery();
            List<Receipt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getReceiptWithUserAndCashbox(resultSet));
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
                result.add(getReceiptWithUserAndCashbox(resultSet));
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
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_RECEIPT_BY_ID_SQL);
            statement.setString(1, receiptId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            var receipt = getReceiptWithUserAndCashbox(resultSet);
            var receiptItems = findReceiptItemsByReceiptId(connection, receiptId);
            receipt.setReceiptItems(receiptItems);
            return Optional.of(receipt);

        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find receipt by id", receiptId), e);
        }
    }

    public Receipt insert(String tellerId, Receipt receipt) {
        receipt.setId(generateUuid());
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_RECEIPT_SQL);
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
            return receipt;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't create receipt with id", receipt.getId()), e);
        }
    }

    public Receipt update(String userId, Receipt receipt) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_RECEIPT_SQL);
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
            return receipt;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update receipt with id", receipt.getId()), e);
        }
    }

    public ReceiptItem insertReceiptItem(
            String receiptId, String productId, ReceiptItem receiptItem) {
        receiptItem.setId(generateUuid());
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_RECEIPT_ITEM_SQL);
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
            return receiptItem;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't create receipt item with id", receiptItem.getId()), e);
        }
    }

    public ReceiptItem updateReceiptItem(
            String receiptId, String productId, ReceiptItem receiptItem) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_RECEIPT_ITEM_SQL);
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
            return receiptItem;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update receipt item with id", receiptItem.getId()), e);
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
                receiptItems.add(getReceiptItemWithProduct(resultSet));
            }
            return receiptItems;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find receipt items by receipt id", receiptId), e);
        }

    }

    private ReceiptItem getReceiptItemWithProduct(ResultSet rs) throws SQLException {
        var receiptItem = new ReceiptItem()
                .setId(rs.getString("id"))
                .setName(rs.getString("name"))
                .setPrice(rs.getBigDecimal("price"))
                .setAmountUnit(
                        ProductAmountUnit.fromExistingInteger(rs.getInt("amount_unit")))
                .setAmount(rs.getBigDecimal("amount"));
        var product = new Product()
                .setId(rs.getString("product_id"))
                .setCode(rs.getString("code"))
                .setAmountUnit(
                        ProductAmountUnit.fromExistingInteger(rs.getInt("amount_unit")))
                .setAmountAvailable(rs.getBigDecimal("amount_available"))
                .setCategory(rs.getString("category"))
                .setDetails(rs.getString("details"))
                .setName(rs.getString("product_name"))
                .setPrice(rs.getBigDecimal("product_price"));
        receiptItem.setProduct(product);
        return receiptItem;
    }

    private Receipt getReceiptWithUser(ResultSet rs) throws SQLException {
        var receipt = new Receipt()
                .setId(rs.getString("id"))
                .setCreatedTime(getLocalDateTime(rs, "created_time"))
                .setCheckoutTime(getLocalDateTimeNullable(rs, "checkout_time").orElse(null))
                .setStatus(ReceiptStatus.fromExistingInteger(rs.getInt("status")))
                .setSumTotal(rs.getBigDecimal("sum_total"));
        var user = new User()
                .setId(rs.getString("user_id"))
                .setFullName(rs.getString("full_name"))
                .setUsername(rs.getString("username"));
        receipt.setUser(user);
        return receipt;
    }

    private Receipt getReceiptWithUserAndCashbox(ResultSet rs) throws SQLException {
        var receipt = getReceiptWithUser(rs);
        var cashbox = new Cashbox()
                .setId(rs.getString("cashbox_id"))
                .setName(rs.getString("name"))
                .setShiftStatus(
                        ShiftStatus.fromExistingInteger(rs.getInt("shift_status")))
                .setShiftStatusTime(getLocalDateTime(rs, "shift_status_time"));
        receipt.getUser().setCashbox(cashbox);
        return receipt;
    }
}
