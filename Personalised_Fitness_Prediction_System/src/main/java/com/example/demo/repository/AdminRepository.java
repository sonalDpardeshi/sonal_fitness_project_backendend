package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.Workout;
import com.example.demo.model.WorkoutCaloriesRelation;

public interface AdminRepository {

	public boolean validateAdmin(String username, String password);

	public List<User> viewUsers();
	
	public boolean deleteUser(Integer userid);

	public boolean suggest(Integer userid);

	public boolean addWorkouts(Workout workout);

	public List<Workout> viewWorkouts();

	public boolean add(WorkoutCaloriesRelation workoutcalories);

	public List<WorkoutCaloriesRelation> view();

	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid);

	public boolean delete(Integer recordid);
	public boolean updateUserStatus(Integer userid, Integer status);

	public List getrequesteduser();
}
