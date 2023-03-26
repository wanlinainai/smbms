package com.kuang.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 梁志超
 * @version 1.0
 * @time 2023/3/25 21:19
 */
public class CharacterEncoding implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("修改编码格式的过滤器开始");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/css;charset=utf-8");
        chain.doFilter(request, response);
    }

    public void destroy() {
        System.out.println("修改编码格式的过滤器停止了");
    }
}
