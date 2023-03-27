package com.kuang.service.user;

import com.kuang.pojo.User;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/26 11:22
 */
public interface UserService {
    //用户登录
    public User login(String userCode, String password);

    //修改用户数据,观察是否修改成功就行了，返回值是boolean就够了
    public boolean updatePwd(int id, String pwd);

    //查询记录数
    public int getUserCount(String username, int userRole);
}
