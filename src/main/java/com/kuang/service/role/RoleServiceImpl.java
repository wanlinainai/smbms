package com.kuang.service.role;

import com.kuang.dao.BaseDao;
import com.kuang.dao.role.RoleDao;
import com.kuang.dao.role.RoleDaoImpl;
import com.kuang.pojo.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/27 14:18
 */
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    //初始化的时候就加载上dao层
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roles = new ArrayList<>();
        try {
            connection = BaseDao.getConnection();
            roles = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //全部关闭
            BaseDao.closeResource(connection, null, null);
        }
        return roles;
    }

    @Test
    public void test(){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
