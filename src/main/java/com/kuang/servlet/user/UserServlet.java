package com.kuang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.kuang.pojo.Role;
import com.kuang.pojo.User;
import com.kuang.service.role.RoleServiceImpl;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;
import com.kuang.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/26 14:46
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //因为本方法不止有一种更新密码的方法，所以说需要把方法独立出去，调用该调用的方法即可
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null) {
            this.updatePwd(req, resp);
        } else if (method.equals("pwdmodify") && method != null) {
            this.pwdModify(req, resp);
        } else if (method.equals("query") && method != null) {
            this.query(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        //从Session里面拿ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;
        //注意此处的StringUtils.isNullOrEmpty(newpassword)如果参数为空才返回TRUE，应该将他变成!StringUtils.isnUllOrEmpty()才对。

        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            //flag为false，更新密码失败，为TRUE，更新成功
            if (flag) {
                req.setAttribute("massage", "您已经成功修改密码，请返回重新登录");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    resp.sendRedirect(req.getContextPath() + "/login.jsp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //移除当前session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("massage", "密码修改出错");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
                req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            req.setAttribute("massage", "新密码有问题");
        }

    }

    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        //前端获得旧密码参数
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if (attribute == null) {//session已经过期
            resultMap.put("result", "sessionerror");
        } else {
            if (StringUtils.isNullOrEmpty(oldpassword)) {//输入为空
                resultMap.put("result", "error");
            } else {//比较
                User user = (User) attribute;
                String userPassword = user.getUserPassword();
                if (userPassword.equals(oldpassword)) {//匹配成功
                    resultMap.put("result", "true");
                } else {
                    resultMap.put("result", "false");
                }
            }
        }
        resp.setContentType("application/json");
        try {
            PrintWriter out = resp.getWriter();
            out.write(JSONArray.toJSONString(resultMap));
            //先刷新后关闭
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //查询用户列表
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;
        List<User> userList = null;
        List<Role> roleList = null;

        UserServiceImpl userService = new UserServiceImpl();
        //第一次走这个请求，一定是第一页，页面大小是固定的
        int currentPageNo = 1;
        int pageSize = 5;
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户的总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();

        //控制首页和尾页
        if (currentPageNo < 1){
            currentPageNo = 1;
        }else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户列表，角色列表
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("queryUsername",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
