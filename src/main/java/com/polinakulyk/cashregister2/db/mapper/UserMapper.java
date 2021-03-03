package com.polinakulyk.cashregister2.db.mapper;

import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.security.dto.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.fromExistingInteger;

public class UserMapper {

    private static final String USER_ID = "id";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ROLE = "role";
    private static final String USER_FULL_NAME = "full_name";

    private static final String CASHBOX_ID = "cashbox_id";
    private static final String CASHBOX_NAME = "name";
    private static final String CASHBOX_SHIFT_STATUS = "shift_status";
    private static final String CASHBOX_SHIFT_STATUS_TIME = "shift_status_time";

    private UserMapper() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static User getUser(ResultSet rs) throws SQLException {
        var user = new User()
                .setId(rs.getString((USER_ID)))
                .setUsername(rs.getString(USER_USERNAME))
                .setPassword(rs.getString(USER_PASSWORD))
                .setRole(UserRole.fromExistingInteger(rs.getInt(USER_ROLE)))
                .setFullName(rs.getString(USER_FULL_NAME));

        var cashbox = new Cashbox()
                .setId(rs.getString(CASHBOX_ID))
                .setShiftStatus(fromExistingInteger(rs.getInt(CASHBOX_SHIFT_STATUS)))
                .setShiftStatusTime(getLocalDateTime(rs, CASHBOX_SHIFT_STATUS_TIME))
                .setName(rs.getString(CASHBOX_NAME));
        user.setCashbox(cashbox);

        return user;
    }
}
