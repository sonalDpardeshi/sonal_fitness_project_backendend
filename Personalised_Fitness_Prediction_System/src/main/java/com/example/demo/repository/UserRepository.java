package com.example.demo.repository;

import java.io.File;
import java.util.*;
import com.example.demo.model.*;

public interface UserRepository {

	public boolean add(User user);

	public boolean validate(String username, String password);

	public boolean fetch(UserWorkoutData userworkout);

	public String readHistory(int userid);

	public double totalCount(int userid);

	public boolean addpath(int userid, File f);

	public List<Workout> viewWorkouts();

	public boolean updateProfile(User user,int userid);

	public List<User> viewProfile(Integer userid);

	


}
