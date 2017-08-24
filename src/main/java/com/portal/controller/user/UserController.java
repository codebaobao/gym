package com.portal.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.common.page.PageReq;
import com.portal.common.page.PageResponse;
import com.portal.entity.User;
import com.portal.service.UserService;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@RestController
@RequestMapping("users")
public class UserController {
	
	private static final ILogger logger = LogUtil.getLogger(LogModule.User, UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/{role}")
	public PageResponse<User> list(@PathVariable String role, @RequestParam String status, @RequestParam String index, @RequestParam String size){
		int pageIndex = Integer.parseInt(index);
		int pageSize = Integer.parseInt(size);
		PageReq pageReq = new PageReq(pageIndex, pageSize);
		
		return userService.list(pageReq, role, status);
	}
	
	@PostMapping
	public void add(@RequestBody User user){
		userService.add(user);
	}
	
	@PutMapping
	public void update(@RequestBody User user){
		userService.update(user);
	}
	
	@DeleteMapping(value="/{id}")
	public void delete(@PathVariable String id){
		userService.delete(id);
	}
}
