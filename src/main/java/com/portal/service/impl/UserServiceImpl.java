package com.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.common.PageReq;
import com.portal.common.Role;
import com.portal.dao.UserRepository;
import com.portal.entity.User;
import com.portal.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void add(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> list(PageReq pageReq, Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User get(String id) {
		return userRepository.findById(id);
	}

	@Override
	public void changePassword(String id, String password) {
		User user = userRepository.findById(id);
		user.setPassword(password);
		userRepository.save(user);
	}

	@Override
	public void delete(String id) {
		userRepository.deleteById(id);
	}

	@Override
	public User getUserByName(String name) {
		return userRepository.findByName(name);
	}

}
