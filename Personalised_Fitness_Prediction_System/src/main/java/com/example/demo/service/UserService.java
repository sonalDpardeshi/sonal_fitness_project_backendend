package com.example.demo.service;

import java.io.File;
import java.util.*;

import com.example.demo.model.User;
import com.example.demo.model.UserWorkoutData;
import com.example.demo.model.Workout;

public interface UserService {

	public boolean add(User user);

	public boolean validate(String username, String password);

public boolean fetch(UserWorkoutData userworkout);

public String readHistory(int userid);

public double totalcount(int userid);

public boolean addPath(int userid, File f);

public List<Workout> viewWorkouts();

public boolean updateProfile(User user,int userid);

public List<User> viewProfile(Integer userid);



}
