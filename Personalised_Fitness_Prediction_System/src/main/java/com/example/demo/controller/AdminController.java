package com.example.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.*;
import com.example.demo.service.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:5173")
public class AdminController {
	
	@Autowired
	AdminService adminservice;
	
	
//	Admin login
	@PostMapping("/login")
	public String adminLogin(@RequestBody Admin admin) {
	return adminservice.validateAdmin(admin.getUsername(),admin.getPassword())?"Admin login success....":"Please check username or password";
	}
	
//	Admin operations
	
//	Workout operations by admin
	
//	Add workouts
	@PostMapping("/workout/add")
	public String addWorkouts(@RequestBody Workout workout) {
		boolean b=adminservice.addWorkouts(workout);
		return b?"Workout added":"workout not added";
	}
	
//	view workouts
	@GetMapping("/workout/view")
	public List<Workout> viewWorkouts(){
		List<Workout> list=adminservice.viewWorkouts();
		return list;
	}
	
//	update or delete workout if some users uses it ??
	
//WorkoutCaloriesRelation related operations by admin
	
//	add 
	@PostMapping("/addWorkoutCalories")
	public String addWorkoutCalories(@RequestBody WorkoutCaloriesRelation workoutcalories) {
		boolean b=adminservice.add(workoutcalories);
		return b?"WorkoutCaloriesRelation added success....":"WorkoutClist;aloriesRelation  not added..";
	}
	
//	view
	@GetMapping("/viewWorkoutCalories")
	public List<WorkoutCaloriesRelation> viewWorkoutCalories(){
		List<WorkoutCaloriesRelation> list=adminservice.view();
		return list;
	}
	
//	update
	@PutMapping("/updateWorkoutCalories/{recordid}")
	public String updateWorkoutCalories(@RequestBody WorkoutCaloriesRelation workoutcalories,@PathVariable("recordid") Integer recordid) {
		boolean b=adminservice.update(workoutcalories,recordid);
		return b?"WorkoutCaloriesRelation updated success...":"WorkoutCaloriesRelation not present...";
	}
	
//	delete 
	@DeleteMapping("/deleteWorkoutCalories/{recordid}")
	public String deleteWorkoutCalories(@PathVariable("recordid") Integer recordid) {
		boolean b=adminservice.delete(recordid);
		return b?"record deleted success...":"record not deleted...";
	}
	
	
	
//	Admin view all users
	@GetMapping("/viewusers")
	public List<User> view()
	{
		List<User> list=adminservice.viewUsers();
 		return list;
	}	

	
//	Recommending workout plan  to individual user by admin
	
//	admin suggesting plan for each user according to its history view by admin
	@GetMapping("/suggest/{userid}")
	public Map<String, LinkedHashSet<String>> recommendPlan(@PathVariable("userid") Integer userid) {
		 Map<String,LinkedHashSet<String>> map=adminservice.suggest(userid);
		if(map!=null) {return map;}
		else {return null;}
	} 
	
	
}
