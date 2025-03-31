package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.User;

public interface AdminRepository {

	public boolean validateAdmin(String username, String password);

	public List<User> view();

}
