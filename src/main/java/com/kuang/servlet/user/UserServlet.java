package com.kuang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.kuang.pojo.User;
import com.kuang.service.user.UserService;
import com.kuang.service.user.UserServiceImpl;
import com.kuang.util.Constants;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
        }else if (method.equals("pwdmodify") && method != null) {
            this.pwdModify(req, resp);
        }else if (method.equals("") && method != null) {

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

    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        //前端获得旧密码参数
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if (attribute == null) {//session已经过期
            resultMap.put("result","sessionerror");
        }else{
            if (StringUtils.isNullOrEmpty(oldpassword)){//输入为空
                resultMap.put("result","error");
            }else{//比较
                User user = (User) attribute;
                String userPassword = user.getUserPassword();
                if (userPassword.equals(oldpassword)){//匹配成功
                    resultMap.put("result","true");
                }else {
                    resultMap.put("result","false");
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
}
