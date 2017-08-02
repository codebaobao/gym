package com.portal.webapp.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/user")
public class TestController {
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String show(@PathVariable("id") Integer id) {
		return "success";
	}
	
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public String update(@PathVariable("id") Integer id) {
		return "success";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") Integer id) {
		return "success";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String add() {
		return "success";
	}
}
