package com.kuang.dao.user;

import com.kuang.dao.BaseDao;
import com.kuang.pojo.User;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
            sql.append("select count(1) as count from smbms.`smbms_user` u,smbms.`smbms_role` r where u.`userRole` = r.`id`");
            List<Object> lists = new ArrayList<>();
            //需要进行模糊查询
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                lists.add("%" + username + "%");
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

    @Override
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        List<User> userList = new ArrayList<>();
        connection = BaseDao.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u, smbms_role r where u.userRole = r.id");
            //用户可以输入一些关键字，进行模糊查询
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //进行分页查询
            sql.append(" order by u.creationDate desc limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println(sql);
            rs = BaseDao.execute(connection, params, sql.toString(), pstm, rs);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }

    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int result = 0;
        if (connection != null) {
            String sql = "insert into smbms.`smbms_user`(`userCode`,`userName`,`userPassword`,`gender`,`birthday`,`phone`,`address`,`userRole`,`creationDate`,`createdBy`)" +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(),user.getUserPassword(),user.getGender(),
            user.getBirthday(),user.getPhone(),user.getAddress(), user.getUserRole(),user.getCreationDate(),user.getCreatedBy()};
            result = BaseDao.execute(connection, params, sql, pstm);
            BaseDao.closeResource(null, pstm, null);
        }
        return result;
    }

    @Override
    public int delete(Connection connection, Integer id) throws SQLException {
        int result = 0;
        PreparedStatement pstm = null;
        if (connection != null) {
            String sql = "delete from `smbms_user` where id=?";
            Object[] params = {id};
            result = BaseDao.execute(connection, params, sql, pstm);
            BaseDao.closeResource(null, pstm, null);
        }
        return result;
    }

    @Override
    public User getUserById(Connection connection, Integer id) throws SQLException {
        User user = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        if (connection != null) {
            //连表查询
            String sql = "select u.*,r.roleName from smbms.`smbms_user` u,smbms.`smbms_role` r where u.userRole=r.id and u.id=?";
            Object[] params = {id};
            rs = BaseDao.execute(connection, params, sql, pstm, rs);
            while(rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setAddress(rs.getString("address"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("useName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    @Override
    public int modify(Connection connection, User user) throws SQLException {
        int result = 0;
        PreparedStatement pstm = null;
        String sql = "update smbms.`smbms_user` set `userName`=?,`gender`=?,`birthday`=?," +
                "`phone`=?,`address`=?,`userRole`=?,modifyBy=?,modifyDate=? where id=?";
        Object[] params = {user.getUserName(),user.getGender(), user.getBirthday(), user.getPhone(),
        user.getAddress(),user.getUserRole(), user.getModifyBy(),user.getModifyDate(), user.getId()};
        result = BaseDao.execute(connection, params, sql, pstm);
        BaseDao.closeResource(null, pstm, null);
        return  result;
    }

    @Test
    public void test(){
        UserDaoImpl userDao = new UserDaoImpl();
        Connection connection = BaseDao.getConnection();
        User user = new User();
        Date date = new Date();
        user.setUserCode("Liangzhichao");
        user.setUserName("梁志超");
        user.setUserPassword("1234567");
        user.setGender(1);
        user.setBirthday(date);
        user.setPhone("15144589652");
        user.setAddress("北京市海淀区");
        user.setUserRole(1);
        user.setCreatedBy(3);
        user.setCreationDate(date);
        user.setModifyBy(3);
        user.setModifyDate(date);
        try {
            userDao.add(connection, user);
            //int result = userDao.delete(connection, 12);
//            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
