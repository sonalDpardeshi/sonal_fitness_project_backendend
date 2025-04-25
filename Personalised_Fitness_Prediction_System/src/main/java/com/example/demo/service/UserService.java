package com.example.demo.service;

import java.io.*;
import java.util.*;
import com.example.demo.model.*;

public interface UserService {

	public boolean add(User user);

	public boolean validate(String username, String password);
	
	public int findUserIdByEmail(String email);
	
	public String findeNameByEmail(String email);

	public boolean fetch(UserWorkoutData userworkout);
	
	public String readHistory(int userid);
	
	public double totalcount(int userid);
	
	public boolean addPath(int userid, String filename);
	
	public String getFilePath(Integer userid);
	
	public List<Workout> viewWorkouts();
	
	public boolean updateProfile(User user,int userid);
	
	public List<User> viewProfile(Integer userid);


}
