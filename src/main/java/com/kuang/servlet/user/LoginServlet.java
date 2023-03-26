package com.kuang.servlet.user;

import com.kuang.pojo.User;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/26 11:48
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("开始登录的servlet");
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //调用业务层与数据库进行比较
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        //有这个人
        if (user != null) {
            //将用户信息保存在session中，方便不同页面进行校验
            //key    value
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //跳转
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            //转发错误
            req.setAttribute("error", "用户名或者密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
