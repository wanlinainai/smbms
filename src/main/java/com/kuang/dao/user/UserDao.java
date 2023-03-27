package com.kuang.dao.user;

import com.kuang.pojo.Role;
import com.kuang.pojo.User;

import javax.management.relation.RoleList;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    //查询用户数量
    public int getCount(Connection connection, String username, int userRole) throws SQLException;

    //查询连表查询的用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException;

    //增删改查
    public int add(Connection connection, User user) throws SQLException;
    public int delete(Connection connection, Integer id) throws SQLException;
    public User getUserById(Connection connection, Integer id) throws SQLException;
    public int modify(Connection connection, User user) throws SQLException;

}
