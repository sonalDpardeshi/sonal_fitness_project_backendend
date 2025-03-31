package com.example.demo.service;

import java.util.*;

import com.example.demo.model.User;

public interface UserService {

	public boolean add(User user);

	public boolean validate(String username, String password);

}
