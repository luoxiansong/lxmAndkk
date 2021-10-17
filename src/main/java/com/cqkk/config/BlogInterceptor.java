package com.cqkk.config;

import com.cqkk.util.SetSessionUtils;
import org.apache.commons.logging .Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: lxmAndkk
 * @description: 自定义拦截器
 * @author: luo kk
 * @create: 2021-05-30 13:40
 */
@Component
public class BlogInterceptor implements HandlerInterceptor {
    private Log log = LogFactory.getLog(BlogInterceptor.class);

    //preHandle在Controller之前执行，因此拦截器的功能主要就是在这个部分实现：
    //检查session中是否有user对象存在；
    //如果存在，就返回true，那么Controller就会继续后面的操作；
    //如果不存在，就会重定向到登录界面。就是通过这个拦截器，使得Controller在执行之前，都执行一遍preHandle.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        log.info("拦截前执行");
        try {
            /*放行所有的测试请求 设置用户名session*/
            SetSessionUtils.setSession(request);
            if (request.getSession().getAttribute("username") != null) {
//                log.info("拦截前执行成功");
                System.out.println(request.getContextPath());
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("拦截前执行失败");
        return false;
        //如果为false，拦截器到此处就不会往下执行了
        //如果为true，继续执行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) {
//        log.info("执行了拦截器的postHandle方法");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) {
//        log.info("执行了拦截器的afterCompletion方法");
    }
}
