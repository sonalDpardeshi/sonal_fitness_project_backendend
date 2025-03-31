package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

public interface AdminService {

	boolean validateAdmin(String username, String password);

	public List<User> view();

}
