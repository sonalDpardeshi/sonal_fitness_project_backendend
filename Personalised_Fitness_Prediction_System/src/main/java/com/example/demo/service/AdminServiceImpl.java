package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.Workout;
import com.example.demo.model.WorkoutCaloriesRelation;
import com.example.demo.repository.AdminRepository;

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
	public boolean suggest(Integer userid) {
		return adminrepo.suggest(userid);
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
	//delete WorkoutCalariesrelation
	@Override
	public boolean delete(Integer recordid) {
		return adminrepo.delete(recordid);
	}
	@Override
	public boolean deleteUser(Integer userid) {
		// TODO Auto-generated method stub
		return adminrepo.deleteUser(userid);
	}
	@Override
	public boolean updateUserStatus(Integer userid, Integer status) {
		// TODO Auto-generated method stub
		return adminrepo.updateUserStatus(userid,status);
	}
	@Override
	public List getrequesteduser() {
		// TODO Auto-generated method stub
		return adminrepo.getrequesteduser();
	}

}
