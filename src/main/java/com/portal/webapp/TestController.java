package com.portal.webapp;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class TestController {
	
	@RequestMapping(value="/hello")
	public String hello(){
		return "hello world!";
	}
}
