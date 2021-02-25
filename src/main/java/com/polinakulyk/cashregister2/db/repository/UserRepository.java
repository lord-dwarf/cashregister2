package com.polinakulyk.cashregister2.db.repository;

import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.polinakulyk.cashregister2.db.DbHelper.*;
import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.fromExistingInteger;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public class UserRepository {

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
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID_SQL);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            User user = getUser(resultSet);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new CashRegisterException(quote("Can't query user by id", userId), e);
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
            User user = getUser(resultSet);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't query user by username", username), e);
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        var user = new User()
                .setId(rs.getString("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .setRole(UserRole.fromExistingInteger(rs.getInt("role")))
                .setFullName(rs.getString("full_name"));
        var cashbox = new Cashbox()
                .setId(rs.getString("cashbox_id"))
                .setShiftStatus(fromExistingInteger(rs.getInt("shift_status")))
                .setShiftStatusTime(getLocalDateTime(rs, "shift_status_time"))
                .setName(rs.getString("name"));
        user.setCashbox(cashbox);
        return user;
    }
}
