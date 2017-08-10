package com.portal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portal.config.WebSecurityConfig;
import com.portal.entity.User;
import com.portal.service.UserService;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@Controller
public class LoginController {
	
	private static final ILogger logger = LogUtil.getLogger(LogModule.Login, LoginController.class); 
	
	@Autowired
	private UserService userService; 
	
    @RequestMapping("/login")
    public String login() throws IOException {
    	return "login";
    }
    
    @RequestMapping("/loginPost")
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

	
    @RequestMapping("/logout")
	public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        // 移除session
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        response.sendRedirect("/login");
	}
}
