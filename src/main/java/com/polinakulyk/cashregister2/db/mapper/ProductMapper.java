package com.polinakulyk.cashregister2.db.mapper;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {

    public static Product getProduct(ResultSet rs) throws SQLException {
        return new Product()
                .setId(rs.getString("id"))
                .setAmountAvailable(rs.getBigDecimal("amount_available"))
                .setAmountUnit(ProductAmountUnit.fromExistingInteger(rs.getInt("amount_unit")))
                .setCategory(rs.getString("category"))
                .setCode(rs.getString("code"))
                .setDetails(rs.getString("details"))
                .setName(rs.getString("name"))
                .setPrice(rs.getBigDecimal("price"));
    }
}
