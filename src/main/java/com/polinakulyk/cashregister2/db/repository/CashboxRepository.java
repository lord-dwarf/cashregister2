package com.polinakulyk.cashregister2.db.repository;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.polinakulyk.cashregister2.db.DbHelper.getConnection;
import static com.polinakulyk.cashregister2.util.CashRegisterUtil.quote;

public class CashboxRepository {
    private static final String UPDATE_CASHBOX_SQL =
            "UPDATE cashbox SET name = ?, shift_status = ?, shift_status_time = ? WHERE id = ?";

    public Cashbox update(Cashbox cashbox) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_CASHBOX_SQL);
            statement.setString(1, cashbox.getName());
            statement.setInt(2, cashbox.getShiftStatus().ordinal());
            statement.setTimestamp(3, DbHelper.toTimestamp(cashbox.getShiftStatusTime()));
            statement.setString(4, cashbox.getId());

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't update cash box with id", cashbox.getId()));
            }
            return cashbox;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update cash box with id", cashbox.getId()), e);
        }
    }
}
