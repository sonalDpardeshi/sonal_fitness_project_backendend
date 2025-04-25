package com.example.demo.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.*;
import com.example.demo.repository.*;

@Service("adminservice")
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository adminrepo;
	@Override
	public boolean validateAdmin(String username, String password) {
		return adminrepo.validateAdmin(username,password);
	}
	@Override
	public List<User> viewUsers() {
		return adminrepo.viewUsers();
	}
	
	@Override
	public List<Intensity> viewIntensities() {
		return adminrepo.viewIntensities();
	}
	
	@Override
	public boolean addWorkouts(Workout workout) {
		return adminrepo.addWorkouts(workout);
	}
	@Override
	public List<Workout> viewWorkouts() {
		return adminrepo.viewWorkouts();
	}
	@Override
	public boolean add(WorkoutCaloriesRelation workoutcalories) {		
		return adminrepo.add(workoutcalories);
	}
	@Override
	public List<WorkoutCaloriesRelation> view() {
		return adminrepo.view();
	}
	@Override
	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid) {
		return adminrepo.update(workoutcalories,recordid);
	}
	@Override
	public boolean delete(Integer recordid) {
		return adminrepo.delete(recordid);
	}
	@Override
	public Map<String, LinkedHashSet<String>> suggest(Integer userid) {
		return adminrepo.suggest(userid);
	}
	@Override
	public List<WorkoutCaloriesRelation> search(String pattern) {
		return adminrepo.search(pattern);
	}
	@Override
	public List<Workout> searchWorkouts(String pattern) {
		return adminrepo.searchWorkouts(pattern);
	}
	
	@Override
	public List<User> searchUser(String pattern) {
		return adminrepo.searchUsers(pattern);
	}

	
}
