package com.kuang.dao.user;

import com.kuang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/26 10:20
 */
public interface UserDao {
    //返回一个用户
    public User getLoginUser(Connection connection, String userCode, String userPassword) throws SQLException;

    //修改当前的用户密码
    public int updatePwd(Connection connection, int id, String userPassword) throws SQLException;
}
