package com.kuang.dao.role;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/27 14:15
 */
public class RoleDaoImpl implements RoleDao{

    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstm = null;
        List<Role> roles = new ArrayList<>();
        if (connection != null) {
            String sql = "select * from smbms.`smbms_role`";
            Object[] params = {};
            rs = BaseDao.execute(connection, params, sql, pstm, rs);
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roles.add(role);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return roles;
    }

}
