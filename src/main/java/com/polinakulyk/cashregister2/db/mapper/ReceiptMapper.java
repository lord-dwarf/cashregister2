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

/**
 * Extracts {@link Receipt} and {@link ReceiptItem} from result set.
 */
public class ReceiptMapper {

    private static final String RECEIPT_ID = "id";
    private static final String RECEIPT_CREATED_TIME = "created_time";
    private static final String RECEIPT_CHECKOUT_TIME = "checkout_time";
    private static final String RECEIPT_STATUS = "status";
    private static final String RECEIPT_SUM_TOTAL = "sum_total";

    private static final String RECEIPT_ITEM_ID = "id";
    private static final String RECEIPT_ITEM_NAME = "name";
    private static final String RECEIPT_ITEM_PRICE = "price";
    private static final String RECEIPT_ITEM_AMOUNT_UNIT = "amount_unit";
    private static final String RECEIPT_ITEM_AMOUNT = "amount";

    private static final String USER_ID = "user_id";
    private static final String USER_FULL_NAME = "full_name";
    private static final String USER_USERNAME = "username";

    private static final String CASHBOX_ID = "cashbox_id";
    private static final String CASHBOX_NAME = "name";
    private static final String CASHBOX_SHIFT_STATUS = "shift_status";
    private static final String CASHBOX_SHIFT_STATUS_TIME = "shift_status_time";

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_CODE = "code";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_AMOUNT_UNIT = "amount_unit";
    private static final String PRODUCT_AMOUNT_AVAILABLE = "amount_available";
    private static final String PRODUCT_CATEGORY = "category";
    private static final String PRODUCT_DETAILS = "details";

    private ReceiptMapper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static ReceiptItem getReceiptItemWithProduct(ResultSet rs) throws SQLException {
        var receiptItem = new ReceiptItem()
                .setId(rs.getString(RECEIPT_ITEM_ID))
                .setName(rs.getString(RECEIPT_ITEM_NAME))
                .setPrice(rs.getBigDecimal(RECEIPT_ITEM_PRICE))
                .setAmountUnit(
                        ProductAmountUnit.fromExistingInteger(rs.getInt(RECEIPT_ITEM_AMOUNT_UNIT)))
                .setAmount(rs.getBigDecimal(RECEIPT_ITEM_AMOUNT));

        var product = new Product()
                .setId(rs.getString(PRODUCT_ID))
                .setCode(rs.getString(PRODUCT_CODE))
                .setAmountUnit(
                        ProductAmountUnit.fromExistingInteger(rs.getInt(PRODUCT_AMOUNT_UNIT)))
                .setAmountAvailable(rs.getBigDecimal(PRODUCT_AMOUNT_AVAILABLE))
                .setCategory(rs.getString(PRODUCT_CATEGORY))
                .setDetails(rs.getString(PRODUCT_DETAILS))
                .setName(rs.getString(PRODUCT_NAME))
                .setPrice(rs.getBigDecimal(PRODUCT_PRICE));
        receiptItem.setProduct(product);

        return receiptItem;
    }

    public static Receipt getReceiptWithUser(ResultSet rs) throws SQLException {
        var receipt = new Receipt()
                .setId(rs.getString(RECEIPT_ID))
                .setCreatedTime(getLocalDateTime(rs, RECEIPT_CREATED_TIME))
                .setCheckoutTime(getLocalDateTimeNullable(rs, RECEIPT_CHECKOUT_TIME).orElse(null))
                .setStatus(ReceiptStatus.fromExistingInteger(rs.getInt(RECEIPT_STATUS)))
                .setSumTotal(rs.getBigDecimal(RECEIPT_SUM_TOTAL));

        var user = new User()
                .setId(rs.getString(USER_ID))
                .setFullName(rs.getString(USER_FULL_NAME))
                .setUsername(rs.getString(USER_USERNAME));
        receipt.setUser(user);
        return receipt;
    }

    public static Receipt getReceiptWithUserAndCashbox(ResultSet rs) throws SQLException {
        var receipt = getReceiptWithUser(rs);
        var cashbox = new Cashbox()
                .setId(rs.getString(CASHBOX_ID))
                .setName(rs.getString(CASHBOX_NAME))
                .setShiftStatus(
                        ShiftStatus.fromExistingInteger(rs.getInt(CASHBOX_SHIFT_STATUS)))
                .setShiftStatusTime(getLocalDateTime(rs, CASHBOX_SHIFT_STATUS_TIME));
        receipt.getUser().setCashbox(cashbox);
        return receipt;
    }
}
