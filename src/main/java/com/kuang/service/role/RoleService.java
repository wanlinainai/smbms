package com.kuang.service.role;

import com.kuang.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/27 14:18
 */
public interface RoleService {
    //获取角色列表
    public List<Role> getRoleList();
}
