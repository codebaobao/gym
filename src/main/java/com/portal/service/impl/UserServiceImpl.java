package com.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.portal.common.PageReq;
import com.portal.common.PageResponse;
import com.portal.common.PageResponseUtil;
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
	public void update(User user){
		userRepository.save(user);
	}

	@Override
	public PageResponse<User> list(PageReq pageReq, String roleName, String status) {
		// TODO Auto-generated method stub
		Pageable pageable = new PageRequest(pageReq.getIndex(), pageReq.getSize(), Sort.Direction.ASC, "id");
		Role role = Role.getRole(roleName);
		Page<User> userPage = userRepository.findByRoleAndStatus(pageable, role, status);
		return PageResponseUtil.getPageResponse(pageReq.getIndex(), pageReq.getSize(), userPage.getTotalPages(), userPage.getContent(),
				userPage.getSize());
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
