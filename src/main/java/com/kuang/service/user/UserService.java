package com.kuang.service.user;

import com.kuang.pojo.User;

import java.util.List;

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

    //根据条件查询用户列表
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize);

    //增删改
    public boolean add(User user);
    public boolean deleteUserById(Integer id);
    public boolean modify(User user);

    //根据ID得到当期那用户的信息
    public User getUserById(Integer id);

    public User selectUserCodeExist(String userCode);
}
