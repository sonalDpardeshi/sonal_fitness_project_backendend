package com.example.demo.service;

import java.util.*;
import com.example.demo.model.*;

public interface AdminService {

	boolean validateAdmin(String username, String password);
	
	public boolean resetAdminCredentials(Admin admin);
	
	public List<Intensity> viewIntensities();

	public boolean addWorkouts(Workout workout);

	public List<Workout> viewWorkouts();

	public boolean add(WorkoutCaloriesRelation workoutcalories);

	public List<WorkoutCaloriesRelation> view();

	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid);
	
	public List<WorkoutCaloriesRelation> search(String pattern);

	public boolean delete(Integer recordid);

	public Map<String,LinkedHashSet<String>> suggest(Integer userid);

	public List<Workout> searchWorkouts(String pattern);
	
	public List<User> viewUsers();

	public List<User> searchUser(String pattern);

}