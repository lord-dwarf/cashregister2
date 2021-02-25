package com.polinakulyk.cashregister2.db.repository;

import com.polinakulyk.cashregister2.db.dto.ProductAmountUnit;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.DbHelper.getConnection;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.*;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public class ProductRepository {
    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO product " +
                    "(id, code, name, category, price, amount_unit, amount_available, details) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String FIND_ALL_PRODUCTS_SQL =
            "SELECT id, amount_available, amount_unit, category, code, details, name, price "
                    + "FROM product";

    private static final String FIND_PRODUCT_BY_ID_SQL =
            "SELECT id, amount_available, amount_unit, category, code, details, name, price "
                    + "FROM product WHERE id = ?";

    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE product " +
                    "SET code = ?, name = ?, category = ?, price = ?, " +
                    "amount_unit = ?, amount_available = ?, details = ? " +
                    "WHERE id = ?;";

    public Product insert(Product product) {
        product.setId(generateUuid());
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_PRODUCT_SQL);
            statement.setString(1, product.getId());
            statement.setString(2, product.getCode());
            statement.setString(3, product.getName());
            statement.setString(4, product.getCategory());
            statement.setBigDecimal(5, product.getPrice());
            statement.setInt(6, product.getAmountUnit().ordinal());
            statement.setBigDecimal(7, product.getAmountAvailable());
            statement.setString(8, product.getDetails());

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't create product with id", product.getId()));
            }
            return product;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't create product with id", product.getId()), e);
        }
    }

    public List<Product> findAll() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_PRODUCTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            List<Product> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(getProduct(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query all products", e);
        }
    }

    public boolean update(Product product) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL);
            statement.setString(1, product.getCode());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setBigDecimal(4, product.getPrice());
            statement.setInt(5, product.getAmountUnit().ordinal());
            statement.setBigDecimal(6, product.getAmountAvailable());
            statement.setString(7, product.getDetails());
            statement.setString(8, product.getId());

            int numOfRows = statement.executeUpdate();
            return numOfRows == 1;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update product with id", product.getId()), e);
        }
    }

    public Optional<Product> findById(String productId) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_BY_ID_SQL);
            statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            var product = getProduct(resultSet);
            return Optional.of(product);

        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find product by id {}", productId), e);
        }
    }

    private Product getProduct(ResultSet rs) throws SQLException {
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
