package com.cqkk.controller;

import com.cqkk.entity.UserInfo;
import com.cqkk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: lxmAndkk
 * @description: 登录Controller
 * @author: luo kk
 * @create: 2021-05-30 13:40
 */
@Controller
@Api(tags = "Login Controller")
@RequestMapping("/admin1")
public class LoginController {

    private Log log = LogFactory.getLog(LoginController.class);

    @Resource
    private UserService userService;

    @GetMapping
    public String loginpage() {
        return "admin/login.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "login.html", notes = "登录逻辑api")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        Model model, HttpServletRequest request) {
        if (username.isEmpty() || password.isEmpty()) {
            log.error("=========用户或密码为空");
            model.addAttribute("msg", "用户或密码为空");
            return "redirect:/admin";
        }
        /*判断是否能够根据用户名获取用户and判断获取到的用户密码是否也是正确的*/
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        List<UserInfo> userInfoList = userService.queryUserInfo(userInfo);
        if (userInfoList.size() != 1) {
            log.error("=========用户名或密码错误");
            model.addAttribute("msg", "用户名或密码错误");
            return "redirect:/admin";
        }
        request.getSession().setAttribute("user", userInfo);
        return "admin/index";
    }

}
