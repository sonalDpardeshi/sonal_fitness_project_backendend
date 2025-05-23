package com.example.demo.service;

import java.io.File;
import java.io.IOException;
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

	@Override
	public boolean updateUser(int id,User user) {
		// TODO Auto-generated method stub
		return userrepo.updateUser(id,user);
	}

	@Override
	public boolean fetch(UserWorkoutData userworkout) {
		return userrepo.fetch(userworkout);
	}

	@Override
	public String readHistory(int userid) {
		return userrepo.readHistory(userid);
	}

	@Override
	public double totalcount(int userid) {
		return userrepo.totalCount(userid);
	}

	@Override
	public boolean addPath(int userid, File f) {
		// TODO Auto-generated method stub
		return userrepo.addpath(userid,f);
	}

	@Override
	public User getUserByEId(String email) {
        return userrepo.getUserByEId(email);
    }

	@Override
	public String viewSuggestedPlan(Integer userid)throws IOException {
		// TODO Auto-generated method stub
		return userrepo.viewSuggestedPlan(userid);
	}

	@Override
	public String requestforplan(int userid) throws Exception {
		// TODO Auto-generated method stub
		return userrepo.requestforplan(userid);
	}

}
