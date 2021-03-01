package com.polinakulyk.cashregister2.db.dao;

import com.polinakulyk.cashregister2.db.Transaction;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.db.mapper.UserMapper;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.ConnectionPool.getConnection;
import static com.polinakulyk.cashregister2.util.Util.quote;

public class UserDao {

    private final static String FIND_BY_USER_ID_SQL =
            "SELECT u.id, u.username, u.password, u.role, u.full_name, u.cashbox_id, "
                    + "c.shift_status, c.shift_status_time, c.name "
                    + "FROM user as u LEFT JOIN cashbox as c ON u.cashbox_id = c.id "
                    + "WHERE u.id = ?";

    private final static String FIND_BY_USERNAME_SQL =
            "SELECT u.id, u.username, u.password, u.role, u.full_name, u.cashbox_id, "
                    + "c.shift_status, c.shift_status_time, c.name "
                    + "FROM user as u LEFT JOIN cashbox as c ON u.cashbox_id = c.id "
                    + "WHERE u.username = ?";

    public Optional<User> findById(String userId) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(FIND_BY_USER_ID_SQL);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            User user = UserMapper.getUser(resultSet);
            var result = Optional.of(user);
            succ = true;
            return result;
        } catch (SQLException e) {
            throw new CashRegisterException(quote("Can't query user by id", userId), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }

    public Optional<User> findByUsername(String username) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            User user = UserMapper.getUser(resultSet);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't query user by username", username), e);
        }
    }
}
