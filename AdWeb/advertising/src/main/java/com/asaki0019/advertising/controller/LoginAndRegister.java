package com.asaki0019.advertising.controller;

import com.asaki0019.advertising.model.User;
import com.asaki0019.advertising.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginAndRegister {

    @Autowired
    private UserService userServiceImpl;

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String name = body.get("name");
        String password = body.get("password");
        String verifiedPassword = body.get("verifiedPassword");

        // 验证参数
        if (username == null || name == null || password == null || !password.equals(verifiedPassword)) {
            return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "用户名、密码和用户名不能为空"));
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
        user.setRole("common");
        // 调用服务层注册用户
        boolean result = userServiceImpl.registerUser(user);
        if (result) {
            return ResponseEntity.ok(Map.of("code", 200, "message", "注册成功", "data", user));
        } else {
            return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "用户已存在"));
        }
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login
    (@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");

        // 验证参数
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("code", 400, "message", "用户名和密码不能为空"));
        }

        // 调用服务层登录用户
        User user = userServiceImpl.loginUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getName());
            data.put("cookie", user.getId()); // 模拟 Cookie
            data.put("role", user.getRole()); // 模拟角色
            return ResponseEntity.ok(Map.of("code", 200, "message", "登陆成功", "data", data));
        } else {
            return ResponseEntity.status(401).body(Map.of("code", 401, "message", "用户名或密码错误"));
        }
    }

    @GetMapping("/exit")
    public ResponseEntity<Map<String, Object>> exitUser
            (HttpSession session){
        session.removeAttribute("user");
        return ResponseEntity.ok(Map.of("code", 200, "message", "退出登录成功"));
    }
}