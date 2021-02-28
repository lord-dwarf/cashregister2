package com.polinakulyk.cashregister2.db.mapper;

import com.polinakulyk.cashregister2.db.entity.Cashbox;
import com.polinakulyk.cashregister2.db.entity.User;
import com.polinakulyk.cashregister2.security.dto.UserRole;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.polinakulyk.cashregister2.db.DbHelper.getLocalDateTime;
import static com.polinakulyk.cashregister2.db.dto.ShiftStatus.fromExistingInteger;

public class UserMapper {

    public static User getUser(ResultSet rs) throws SQLException {
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
