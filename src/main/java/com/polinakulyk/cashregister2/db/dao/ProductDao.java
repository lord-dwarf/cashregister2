package com.polinakulyk.cashregister2.db.dao;

import com.polinakulyk.cashregister2.db.Transaction;
import com.polinakulyk.cashregister2.db.entity.Product;
import com.polinakulyk.cashregister2.db.mapper.ProductMapper;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.polinakulyk.cashregister2.controller.dto.HttpStatus.BAD_REQUEST;
import static com.polinakulyk.cashregister2.db.ConnectionPool.getConnection;
import static com.polinakulyk.cashregister2.util.Util.generateUuid;
import static com.polinakulyk.cashregister2.util.Util.quote;

public class ProductDao {
    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO product " +
                    "(id, code, name, category, price, amount_unit, amount_available, details) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String FIND_ALL_PRODUCTS_SQL =
            "SELECT id, amount_available, amount_unit, category, code, details, name, price "
                    + "FROM product";

    private static final String FIND_PRODUCTS_WITH_PAGINATION_SQL =
            "SELECT id, amount_available, amount_unit, category, code, details, name, price "
                    + "FROM product ORDER BY name ASC LIMIT ? OFFSET ?";

    private static final String COUNT_PRODUCTS_SQL =
            "SELECT count(1) as count from product";

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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CashRegisterException(
                    BAD_REQUEST,
                    quote("Can't create product with duplicate code", product.getCode()),
                    e);
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
                result.add(ProductMapper.getProduct(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query all products", e);
        }
    }

    public List<Product> findWithPagination(int rowsLimit, int rowsOffset) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(FIND_PRODUCTS_WITH_PAGINATION_SQL);
            statement.setInt(1, rowsLimit);
            statement.setInt(2, rowsOffset);
            ResultSet resultSet = statement.executeQuery();
            List<Product> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(ProductMapper.getProduct(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException("Can't query products with pagination", e);
        }
    }

    public int count() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(COUNT_PRODUCTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new CashRegisterException("Can't count products");
            }
            return resultSet.getInt("count");
        } catch (SQLException e) {
            throw new CashRegisterException("Can't count products", e);
        }
    }

    public boolean update(Product product) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_PRODUCT_SQL);
            statement.setString(1, product.getCode());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setBigDecimal(4, product.getPrice());
            statement.setInt(5, product.getAmountUnit().ordinal());
            statement.setBigDecimal(6, product.getAmountAvailable());
            statement.setString(7, product.getDetails());
            statement.setString(8, product.getId());

            int numOfRows = statement.executeUpdate();

            succ = true;
            return numOfRows == 1;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update product with id", product.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public Optional<Product> findById(String productId) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_PRODUCT_BY_ID_SQL);
            statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                succ = true;
                return Optional.empty();
            }
            var product = ProductMapper.getProduct(resultSet);
            var result = Optional.of(product);

            succ = true;
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't find product by id {}", productId), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }
}
