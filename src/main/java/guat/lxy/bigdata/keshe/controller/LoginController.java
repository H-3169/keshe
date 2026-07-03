package guat.lxy.bigdata.keshe.controller;

import guat.lxy.bigdata.keshe.entity.User;
import guat.lxy.bigdata.keshe.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String expired,
            HttpServletRequest request,
            HttpServletResponse response) {

        // ★★★ 如果是从前进/后退进来的，强制清除 session ★★★
        if ("true".equals(expired)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "login";
        }

        // 如果已登录，跳转到首页
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/index";
        }
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
        user.setPassword("{noop}" + user.getPassword());
        user.setActive(1);
        userMapper.insert(user);
        userMapper.assignRole(user.getId(), 2);
        model.addAttribute("success", "注册成功，请登录");
        return "login";
    }
}