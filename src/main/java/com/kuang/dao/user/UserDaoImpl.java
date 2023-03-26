package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/26 10:37
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode, String userPassword) throws SQLException {

        PreparedStatement pre = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms.smbms_user where userCode=? and userPassword=?";
            Object[] params = {userCode,userPassword};
            rs = BaseDao.execute(connection, params, sql, pre, rs);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setAddress(rs.getString("address"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            //关闭资源
            BaseDao.closeResource(null,pre,rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String userPassword) throws SQLException {
        PreparedStatement pstm = null;
        int execute =0;
        if (connection != null) {
            String sql = "update smbms.smbms_user set userPassword=? where id = ?";

            Object[] params = {userPassword, id};
            execute = BaseDao.execute(connection, params, sql, pstm);
            BaseDao.closeResource(null, pstm,null);
        }
        //如果大于0就说明已经成功了
        return execute;
    }
}
