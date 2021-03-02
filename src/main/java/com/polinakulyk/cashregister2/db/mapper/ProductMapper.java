package com.polinakulyk.cashregister2.db.mapper;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {

    private ProductMapper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    private static final String PRODUCT_ID = "id";
    private static final String PRODUCT_AMOUNT_AVAILABLE = "amount_available";
    private static final String PRODUCT_AMOUNT_UNIT = "amount_unit";
    private static final String PRODUCT_CATEGORY = "category";
    private static final String PRODUCT_CODE = "code";
    private static final String PRODUCT_DETAILS = "details";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";


    public static Product getProduct(ResultSet rs) throws SQLException {
        return new Product()
                .setId(rs.getString(PRODUCT_ID))
                .setAmountAvailable(rs.getBigDecimal(PRODUCT_AMOUNT_AVAILABLE))
                .setAmountUnit(ProductAmountUnit.fromExistingInteger(rs.getInt(PRODUCT_AMOUNT_UNIT)))
                .setCategory(rs.getString(PRODUCT_CATEGORY))
                .setCode(rs.getString(PRODUCT_CODE))
                .setDetails(rs.getString(PRODUCT_DETAILS))
                .setName(rs.getString(PRODUCT_NAME))
                .setPrice(rs.getBigDecimal(PRODUCT_PRICE));
    }
}
