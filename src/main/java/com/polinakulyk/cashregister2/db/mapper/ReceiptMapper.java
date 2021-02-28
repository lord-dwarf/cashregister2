package com.polinakulyk.cashregister2.db.mapper;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.dto.ReceiptStatus;
import com.polinakulyk.cashregister2.db.dto.ShiftStatus;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.entity.Receipt;
import com.polinakulyk.cashregister2.db.entity.ReceiptItem;
import com.polinakulyk.cashregister2.db.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTimeNullable;

public class ReceiptMapper {

    public static ReceiptItem getReceiptItemWithProduct(ResultSet rs) throws SQLException {
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

    public static Receipt getReceiptWithUser(ResultSet rs) throws SQLException {
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

    public static Receipt getReceiptWithUserAndCashbox(ResultSet rs) throws SQLException {
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
