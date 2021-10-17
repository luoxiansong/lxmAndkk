package com.cqkk.util;

import javax.servlet.http.HttpServletRequest;

public class SetSessionUtils {
    /*因为这只了拦截器 所以每次设定一个用户名放入session中*/
    public static void setSession(HttpServletRequest request) {
        request.getSession().setAttribute("username", "admin");
        return;
    }
}
