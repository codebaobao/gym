//package com.portal.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.portal.entity.SecurityUser;
//import com.portal.entity.User;
//import com.portal.service.UserService;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class CustomUserDetailsService implements UserDetailsService {
//    @Autowired  //数据库服务类
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        User user = userService.getUserByName(userName);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("UserName " + userName + " not found");
//        }
//
//        SecurityUser securityUser = new SecurityUser(user);
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        return securityUser; //code9
//
//    }
//
//}