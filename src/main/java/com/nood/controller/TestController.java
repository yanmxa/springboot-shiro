package com.nood.controller;

import com.nood.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class TestController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) subject.logout();
        return "login";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin success !";
    }

    @RequestMapping("/edit")
    @ResponseBody
    public String edit() {
        return "edit success !";
    }

    @RequestMapping("/loginUser")
    public String loginUser(@RequestParam("username") String userName,
                            @RequestParam("password") String password,
                            HttpSession session) {

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject(); // securityManager根据自定义的AuthRealm来进行登录认证/授权

        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            session.setAttribute("user", user); //仅仅挂载到上下文中，jsp中可以取到
            return "index";
        } catch (Exception e) {
            return "login";
        }
    }

    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

}
