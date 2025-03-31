package com.example.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {
	
	@Autowired
	AdminService adminservice;
	
	
//	Admin login
	@PostMapping("/login/{username}/{password}")
	public String adminLogin(@PathVariable("username") String username,@PathVariable("password") String password) {
	return adminservice.validateAdmin(username,password)?"Admin login success....":"Please check username or password";
	}
	
//	Admin operations
	
//	Workout operations by admin
	
	@Autowired
	WorkoutService workoutservice;
//	Add workouts
	@PostMapping("/workout/add")
	public String addWorkouts(@RequestBody Workout workout) {
		boolean b=workoutservice.add(workout);
		return b?"Workout added":"workout not added";
	}
	
//	view workouts
	@GetMapping("/workout/view")
	public List<Workout> viewWorkouts(){
		List<Workout> list=workoutservice.view();
		return list;
	}
	
//	update or delete workout if some users uses it ??
	
//WorkoutCaloriesRelation related operations by admin
	
	@Autowired
	WorkoutCaloriesRelationService WorkoutCaloriesRelationservice;
	
//	add 
	@PostMapping("/addWorkoutCalories")
	public String addWorkoutCalories(@RequestBody WorkoutCaloriesRelation workoutcalories) {
		boolean b=WorkoutCaloriesRelationservice.add(workoutcalories);
		return b?"WorkoutCaloriesRelation added success....":"WorkoutClist;aloriesRelation  not added..";
	}
	
//	view
	@GetMapping("/viewWorkoutCalories")
	public List<WorkoutCaloriesRelation> viewWorkoutCalories(){
		List<WorkoutCaloriesRelation> list=WorkoutCaloriesRelationservice.view();
		return list;
	}
	
//	update
	@PutMapping("/updateWorkoutCalories/{id}")
	public String updateWorkoutCalories(@RequestBody WorkoutCaloriesRelation workoutcalories,@PathVariable("id") Integer record_id) {
		boolean b=WorkoutCaloriesRelationservice.update(workoutcalories,record_id);
		return b?"WorkoutCaloriesRelation updated success...":"WorkoutCaloriesRelation not present...";
	}
	
//	delete 
	@DeleteMapping("/deleteWorkoutCalories/{id}")
	public String deleteWorkoutCalories(@PathVariable("id") Integer recordid) {
		boolean b=WorkoutCaloriesRelationservice.delete(recordid);
		return b?"record deleted success...":"record not deleted...";
	}
	
	
	
//	Admin view all users
	@GetMapping("/viewusers")
	public List<User> view()
	{
		List<User> list=adminservice.view();
 		return list;
	}	

	
//	workout_intensity_duration_cal_burn add view by admin
	
}
