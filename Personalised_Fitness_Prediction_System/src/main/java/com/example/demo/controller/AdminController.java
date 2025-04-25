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
import com.example.demo.Exceptions.*;
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
	public Map<String,Object> adminLogin(@RequestBody Admin admin) {
	
		boolean b= adminservice.validateAdmin(admin.getUsername(),admin.getPassword());
		Map<String,Object> m=new HashMap<>();
	if(b) {
		m.put("msg","Admin login success....");
		}
	else {
		 throw new AdminNotFoundException("Invalid username or password");
	}
	return m;
	}
	
	
//	Admin operations
	
//	view intensities
	@GetMapping("/intensities")
	public List<Intensity> viewIntensities(){
		List<Intensity> list=adminservice.viewIntensities();
		return list;
	}
	
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
	
	//search Workouts
	@GetMapping("searchworkout/{pattern}")
	public List<Workout> viewWorkouts(@PathVariable("pattern") String pattern){
		List<Workout> list=adminservice.searchWorkouts(pattern);
		return list;
	}
	
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
//		System.out.println(list.get(0).getRecordid());
		return list;
	}
	
//	update
	@PutMapping("/updateWorkoutCalories/{recordid}")
	public String updateWorkoutCalories(@RequestBody WorkoutCaloriesRelation workoutcalories,@PathVariable("recordid") Integer recordid) {
		boolean b=adminservice.update(workoutcalories,recordid);
		return b?"WorkoutCaloriesRelation updated success...":"WorkoutCaloriesRelation not present...";
	}
	
	//search
	@GetMapping("/searchWorkoutCalories/{pattern}")
	public List<WorkoutCaloriesRelation> searchWorkoutCalories(@PathVariable("pattern") String pattern){
		List<WorkoutCaloriesRelation> list=adminservice.search(pattern);
		return list;
	}
	
	
//	delete not need to do 
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
	
//	search users
	@GetMapping("/searchuser/{pattern}")
	public List<User> searchUser(@PathVariable("pattern") String pattern){
		List<User> list=adminservice.searchUser(pattern);
		return list;
	}
	
//	Recommending workout plan  to individual user by admin
	
//	admin suggesting plan for each user according to its history view by admin
	@GetMapping("/suggest/{userid}")
	public Map<String, LinkedHashSet<String>> recommendPlan(@PathVariable("userid") Integer userid) {
		 Map<String,LinkedHashSet<String>> map=adminservice.suggest(userid);
//		 map.forEach((e,i)->{
//			 System.out.println(e+" "+i);
//		 });
		return map;
	} 

}
