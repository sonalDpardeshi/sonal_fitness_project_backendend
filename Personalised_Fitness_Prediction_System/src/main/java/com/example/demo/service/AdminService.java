package com.example.demo.service;

import java.util.*;
import com.example.demo.model.*;

public interface AdminService {

	boolean validateAdmin(String username, String password);

	public List<User> viewUsers();

	public boolean addWorkouts(Workout workout);

	public List<Workout> viewWorkouts();

	public boolean add(WorkoutCaloriesRelation workoutcalories);

	public List<WorkoutCaloriesRelation> view();

	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid);

	public boolean delete(Integer recordid);

	public Map<String,LinkedHashSet<String>> suggest(Integer userid);

}
