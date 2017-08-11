package com.portal.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.common.PageReq;
import com.portal.common.PageResponse;
import com.portal.entity.User;
import com.portal.service.UserService;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@RestController
@RequestMapping("users")
public class UserController {
	
	private static final ILogger logger = LogUtil.getLogger(LogModule.Login, UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/{role}")
	public PageResponse<User> list(@PathVariable String role, @RequestParam String index, @RequestParam String size){
		int pageIndex = Integer.parseInt(index);
		int pageSize = Integer.parseInt(size);
		PageReq pageReq = new PageReq(pageIndex, pageSize);
		
		return userService.list(pageReq, role);
	}
	
	@PostMapping
	public void add(User user){
		userService.add(user);
	}
	
	@PutMapping
	public void update(User user){
		userService.update(user);
	}
	
	@DeleteMapping(value="/{id}")
	public void delete(@PathVariable String id){
		userService.delete(id);
	}
}
