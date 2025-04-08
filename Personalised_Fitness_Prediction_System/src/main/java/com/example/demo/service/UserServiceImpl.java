package com.example.demo.service;

import java.io.File;
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
	public List<Workout> viewWorkouts() {
		return userrepo.viewWorkouts();
	}

	@Override
	public boolean updateProfile(User user,int userid) {
		return userrepo.updateProfile(user,userid);
	}

	@Override
	public List<User> viewProfile(Integer userid) {
		return userrepo.viewProfile(userid);
	}

}
