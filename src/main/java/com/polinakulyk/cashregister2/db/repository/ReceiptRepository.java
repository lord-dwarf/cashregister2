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
import com.polinakulyk.cashregister2.util.CashRegisterUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.DbHelper.getConnection;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTimeNullable;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.generateUuid;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public class ReceiptRepository {

    private static final String FIND_ALL_RECEIPTS_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, r.user_id, "
                    + "u.full_name, u.username, u.cashbox_id, "
                    + "c.name, c.shift_status, c.shift_status_time "
                    + "FROM receipt AS r LEFT JOIN user AS u ON r.user_id = u.id "
                    + "LEFT JOIN cashbox AS c ON u.cashbox_id = c.id;";

    private static final String FIND_RECEIPT_BY_ID_SQL =
            "SELECT r.id, r.created_time, r.checkout_time, r.status, r.sum_total, " +
                    "r.user_id, u.username, u.full_name FROM receipt AS r " +
                    "LEFT JOIN user AS u ON r.user_id = u.id " +
                    "WHERE r.id = ?;";

    private static final String FIND_RECEIPT_ITEMS_BY_RECEIPT_ID_SQL =
            "SELECT ri.id, ri.name, ri.price, ri.amount_unit, ri.amount, p.code " +
                    "FROM receipt_item AS ri LEFT JOIN product AS p ON ri.product_id = p.id " +
                    "WHERE ri.receipt_id = ?;";

    private static final String INSERT_RECEIPT_SQL =
            "INSERT INTO receipt " +
                    "(id, created_time, checkout_time, status, sum_total, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?);";

    public List<Receipt> findAll() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_RECEIPTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            List<Receipt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getReceiptWithCashbox(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query all receipts", e);
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
            var receipt = getReceiptWithUser(resultSet);
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
            CashRegisterUtil a = null;
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
                .setAmountUnit(ProductAmountUnit.fromExistingInteger(rs.getInt("amount_unit")))
                .setAmount(rs.getBigDecimal("amount"));
        var product = new Product()
                .setCode(rs.getString("code"));
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

    private Receipt getReceiptWithCashbox(ResultSet rs) throws SQLException {
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
