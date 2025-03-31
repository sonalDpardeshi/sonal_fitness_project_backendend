package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.demo.model.*;
import com.example.demo.service.*;

@RestController
@RequestMapping("/user")
public class UserController {
//	service object
	@Autowired
	UserService userservice;
	
	
//	User register
	@PostMapping("/register")
	public String userRegister(@RequestBody User user) {
		System.out.println("controller: "+user.getEmail());
		boolean b=userservice.add(user);
		if(b) {
		return "User registration success....";
		}
		else {
			return "User registration failed";
		}
	}
	
//	user login 
	@PostMapping("/login")
	public String userLogin(@RequestBody User user){//@RequestParam("username") String username,@RequestParam("password") String password) {
		boolean b=userservice.validate(user.getEmail(),user.getPassword());
		if(b)
		{
			return "User login success....";
		}
		else {
			return "Please check username or password";
		}
	}
	

	
//	@GetMapping("/view")
//	public List<Map<String,Object>> view(){
//		List<User> list=userservice.view();
//		return list.stream().map(u->{
//			Map<String,Object> map=new HashMap<>();
//			map.put("name",u.getName());
//			map.put("email", u.getEmail());
//			map.put("height", u.getHeight());
//			map.put("weight", u.getWeight());
//			return map;
//		}).collect(Collectors.toList());
//		
//		list.forEach(e->System.out.println(e.getUserid()+" "+e.getEmail()+" "+e.getPassword()));
//		return list;
//		
//	}
}
