package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;

@Service("adminservice")
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository adminrepo;
	@Override
	public boolean validateAdmin(String username, String password) {
		return adminrepo.validateAdmin(username,password);
	}
	@Override
	public List<User> view() {
		return adminrepo.view();
	}

}
