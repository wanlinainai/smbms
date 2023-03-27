package com.kuang.dao.role;

import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/27 14:15
 */
public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
