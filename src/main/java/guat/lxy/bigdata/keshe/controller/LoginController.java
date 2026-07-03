package guat.lxy.bigdata.keshe.controller;

import guat.lxy.bigdata.keshe.entity.User;
import guat.lxy.bigdata.keshe.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        User existing = userMapper.findByUsername(user.getUsername());
        if (existing != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
        // ★★★ 自动加 {noop} 前缀 ★★★
        user.setPassword("{noop}" + user.getPassword());
        user.setActive(1);
        userMapper.insert(user);
        userMapper.assignRole(user.getId(), 2);
        model.addAttribute("success", "注册成功，请登录");
        return "login";
    }
}