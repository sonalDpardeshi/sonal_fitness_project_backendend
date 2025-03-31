package com.example.demo.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.*;
import com.example.demo.repository.*;

@Service("userservice")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userrepo;
	
	@Override
	public boolean add(User user) {
		return userrepo.add(user);
	}

	@Override
	public boolean validate(String username, String password) {
		return userrepo.validate(username,password);
	}

}
