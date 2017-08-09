package com.portal.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter{
	
	public final static String SESSION_KEY = "user";
	
	public final static String LOGIN_URL = "/login";
	
	public final static String LOGIN_ERROR = "/error";
	
	@Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns(LOGIN_ERROR);
        addInterceptor.excludePathPatterns(LOGIN_URL+"**");

        // 拦截配置
//        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null)
                return true;

            // 跳转登录
//            response.sendRedirect("login.html");
//            gotoPage(response, "/login.html");
            return true;
        }
    	private void gotoPage(HttpServletResponse res, String page) throws IOException{
    		String html = "<html><head>"
    				+ "<meta http-equiv=\"Refresh\" content=\"0; URL=" + page
    				+ "\">" + "</head></html>";

    		res.addDateHeader("Date", new java.util.Date().getTime());
    		res.addHeader("Content-Type", "	text/html; charset=utf-8");
    		res.addHeader("Connection", "keep-alive");
    		res.addHeader("Pragma", "no-cache");
    		res.addHeader("Cache-Control", "no-cache; private; no-store");
    		res.setStatus(HttpServletResponse.SC_OK);
    		res.getWriter().print(html);
    		res.flushBuffer();
    	}
    }

}
