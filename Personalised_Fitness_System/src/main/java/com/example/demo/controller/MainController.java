package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;

import java.util.*;

@RestController
public class MainController {
	
//	View available types
	@GetMapping("/menu")
	public String getMenu() {
		return "choose type: \n 1.Admin \n 2. User";
	}
	
//	Choose who you are??
	@PostMapping("menu/{menuid}")
	public String chooseType(@PathVariable("menuid") Integer menuid){
		switch(menuid) {
		case 1: return "You are admin ";
		case 2: return "You are user";
		default: return "Invalid choice ";
		}
	}
	

	
}