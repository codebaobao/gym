package com.portal.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.webapp.config.WebSecurityConfig;
import com.portal.webapp.entity.User;
import com.portal.webapp.service.UserService;
import com.portal.webapp.utils.log.ILogger;
import com.portal.webapp.utils.log.LogModule;
import com.portal.webapp.utils.log.LogUtil;

@RestController
public class LoginController {
	
	private static final ILogger logger = LogUtil.getLogger(LogModule.Login, LoginController.class); 
	
	@Autowired
	private UserService userService; 
	
	@GetMapping("/")
	public String index() {
		return "success";
	}
	
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/loginPost")
    public @ResponseBody Map<String, Object> loginPost(String name, String password, HttpSession session) {
    	User user = userService.getUserByName(name);
    	Map<String, Object> map = new HashMap<>();
        if (!password.equals(user.getPassword())) {
            map.put("flag", "fail");
            return map;
        }

        // 设置session
        session.setAttribute(WebSecurityConfig.SESSION_KEY, name);

        map.put("flag", "success");
        map.put("user", user);
        logger.info("login success!");
        return map;
    }

	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/login";
	}
}
