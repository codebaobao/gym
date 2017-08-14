package com.portal.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portal.entity.User;
import com.portal.service.UserService;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@Controller
public class ShiroController {

    private static final ILogger logger = LogUtil.getLogger(LogModule.Login, ShiroController.class);
    
	@Autowired
	private UserService userService;

    @RequestMapping("/index")
    public String index(){
    	return "index";
    }
    
    @RequestMapping(value="/login")
    public String login(){
    	return "login";
    }

    @RequestMapping(value="/doLogin", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> loginPost(@RequestParam String username, @RequestParam String password){
    	Map<String, Object> map = new HashMap<>();
    	String message = "success";
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //获取当前的Subject  
        Subject currentUser = SecurityUtils.getSubject();  
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查  
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应  
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法  
            logger.info("对用户[" + username + "]进行登录验证..验证开始");  
            currentUser.login(token);  
            logger.info("对用户[" + username + "]进行登录验证..验证通过");  
        }catch(UnknownAccountException uae){  
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");  
            message = "未知账户";
        }catch(IncorrectCredentialsException ice){  
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");  
            message = "密码不正确";
        }catch(LockedAccountException lae){  
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");  
            message = "账户已锁定";
        }catch(ExcessiveAttemptsException eae){  
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");  
            message = "用户名或密码错误次数过多";
        }catch(AuthenticationException ae){  
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景  
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");  
            ae.printStackTrace();
            message = "用户名或密码不正确";
        } 
        //验证是否登录成功  
        if(currentUser.isAuthenticated()){
            User user = userService.getUserByName(username);
            map.put("user", user);
        }else{  
            token.clear();  
        }
        map.put("message", message);
        return map;
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)  
    public String logout(){ 
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();  
        return "redirect:/login";
    } 

    @RequestMapping("/403")
    public String unauthorizedRole(){
        logger.info("------没有权限-------");
        return "403";
    }
}