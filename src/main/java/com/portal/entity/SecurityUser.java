//package com.portal.entity;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.portal.common.Role;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class SecurityUser extends User implements UserDetails {
//
//
//    private static final long serialVersionUID = 1L;
//
//    public SecurityUser(User user) {
//
//        if (user != null)
//
//        {
//            this.setId(user.getId());
//
//            this.setName(user.getName());
//
//            this.setPassword(user.getPassword());
//
//            this.setRole(user.getRole());
//        }
//
//    }
//
//
//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//
//
//        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//
//        Role userRole = this.getRole();
//
//
//        if (userRole != null)
//
//        {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+userRole.name());
//
//            authorities.add(authority);
//        }
//
//        return authorities;
//
//    }
//
//
//    @Override
//    public String getPassword() {
//
//        return super.getPassword();
//
//    }
//
//
//    @Override
//    public String getUsername() {
//
//        return super.getEmail();
//
//    }
//
//
//    @Override
//    public boolean isAccountNonExpired() {
//
//        return true;
//
//    }
//
//
//    @Override
//    public boolean isAccountNonLocked() {
//
//        return true;
//
//    }
//
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//
//        return true;
//
//    }
//
//
//    @Override
//    public boolean isEnabled() {
//
//        return true;
//
//    }
//
//
//}