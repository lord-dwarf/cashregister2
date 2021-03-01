package com.polinakulyk.cashregister2.db.dao;

import com.polinakulyk.cashregister2.db.DbHelper;
import com.polinakulyk.cashregister2.db.Transaction;
import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.exception.CashRegisterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.util.Util.quote;

public class CashboxDao {
    private static final String UPDATE_CASHBOX_SQL =
            "UPDATE cashbox SET name = ?, shift_status = ?, shift_status_time = ? WHERE id = ?";

    public Cashbox update(Cashbox cashbox) {
        Connection conn = null;
        boolean succ = false;
        try {
            conn = Transaction.getTransactionalConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_CASHBOX_SQL);
            statement.setString(1, cashbox.getName());
            statement.setInt(2, cashbox.getShiftStatus().ordinal());
            statement.setTimestamp(3, DbHelper.toTimestamp(cashbox.getShiftStatusTime()));
            statement.setString(4, cashbox.getId());

            int numOfRows = statement.executeUpdate();
            if (numOfRows != 1) {
                throw new CashRegisterException(
                        quote("Can't update cash box with id", cashbox.getId()));
            }
            succ = true;
            return cashbox;
        } catch (SQLException e) {
            throw new CashRegisterException(
                    quote("Can't update cash box with id", cashbox.getId()), e);
        } finally {
            Transaction.rollbackIfNeeded(conn, succ);
        }
    }
}