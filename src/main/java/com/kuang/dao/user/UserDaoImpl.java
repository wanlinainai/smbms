package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.mysql.cj.util.StringUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            Object[] params = {userCode, userPassword};
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
            BaseDao.closeResource(null, pre, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String userPassword) throws SQLException {
        PreparedStatement pstm = null;
        int execute = 0;
        if (connection != null) {
            String sql = "update smbms.smbms_user set userPassword=? where id = ?";

            Object[] params = {userPassword, id};
            execute = BaseDao.execute(connection, params, sql, pstm);
            BaseDao.closeResource(null, pstm, null);
        }
        //如果大于0就说明已经成功了
        return execute;
    }

    //根据用户名或者角色查询用户总数
    @Override
    public int getCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        StringBuffer sql = new StringBuffer();
        ResultSet rs = null;
        int count = 0;
        if (connection != null) {
            sql.append("select count(*) as count from smbms.`smbms_user` u,smbms.`smbms_role` r where u.`userRole` = r.`id`");
            List<Object> lists = new ArrayList<>();
            //需要进行模糊查询
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append("and u.userName like ?");
                lists.add("%" + username+ "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole=?");
                lists.add(userRole);
            }
            Object[] params = lists.toArray();
            System.out.println("getCountSQL:" + sql);
            rs = BaseDao.execute(connection, params, sql.toString(), pstm, rs);
            while (rs.next()) {
                count = rs.getInt("count");
                //因为查询结果只是一列，就是数量列，rs也就只有一个数，count，查询不到其他表的数据
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return count;
    }

    @Test
    public void test(){
        Connection connection = BaseDao.getConnection();
        String username = "张";
        int userRole = 0;

        try {
            int count = getCount(connection, username, userRole);
            System.out.println(count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
