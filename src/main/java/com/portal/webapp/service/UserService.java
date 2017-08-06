package com.portal.webapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portal.webapp.common.PageReq;
import com.portal.webapp.common.Role;
import com.portal.webapp.entity.User;

@Service
public interface UserService {
    /**
     * Create User
     * @param user
     * @return
     */
    public void add(User user);

    /**
     * 获取Users
     * @return
     */
    public List<User> list(PageReq pageReq, Role role);

    /**
     * 按id获取User
     * @return
     */
    public User get(String id);

    /**
     *
     * @param id
     * @param password
     * @return
     */
    public void changePassword(String id,String password);

    /**
     * 删除User
     * @param id
     */
    public void delete(String id);

    User getUserByName(String name);
}
