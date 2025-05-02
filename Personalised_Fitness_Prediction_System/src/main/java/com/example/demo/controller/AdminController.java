package com.example.demo.controller;

import java.io.File;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.*;
import com.example.demo.repository.WorkoutCaloriesRelationRepository;
import com.example.demo.service.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
	
	@Autowired
	AdminService adminservice;
	@Autowired
	IntensityService intensityservice;
	@Autowired
	WorkoutService workoutservice;
	@Autowired
	WorkoutCaloriesRelationRepository wcrrepo;

//	Admin login
	@PostMapping("/login/{username}/{password}")
	public String adminLogin(@PathVariable("username") String username,@PathVariable("password") String password) {
	return adminservice.validateAdmin(username,password)?"Admin login success....":"Please check username or password";
	}
	
	
//	Workout operations by admin
//	New Add Workouts
	@PostMapping("/workout/add")
	public String addWorkouts(@RequestBody Workout workout)
	{
		boolean b = workoutservice.add(workout);
		return b?"Workout added":"workout not added";
	}
//	view workouts
	@GetMapping("/workout/view")
	public List<Workout> viewWorkouts(){
		List<Workout> list=workoutservice.view();
		return list;
	}
	
	//view Intensity
	@GetMapping("/intensity/view")
	public List<Intensity> viewIntensity()
	{
		List<Intensity> list=intensityservice.viewIntensity();
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
//	@DeleteMapping("/deleteWorkoutCalories/{recordid}")
//	public String deleteWorkoutCalories(@PathVariable("recordid") Integer recordid) {
//		boolean b=adminservice.delete(recordid);
//		return b?"record deleted success...":"record not deleted...";
//	}
	
	
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
	public String recommendPlan(@PathVariable("userid") Integer userid) {
		
		boolean b=adminservice.suggest(userid);
		if(b) {return "Recommended Plan for "+userid;}
		else {return "User Not Perform any Workout"+userid;}
//		return "plan suggested done";
	} 
	
	//get workoutcalories by recordid
	
	@GetMapping("/viewWorkoutCaloriesbyrecordid/{recordid}")
	public WorkoutCaloriesRelation getWorkoutCaloriesbyrid(@PathVariable Integer recordid) {
	    WorkoutCaloriesRelation wcr = wcrrepo.getworkoutcalbyrid(recordid);
	    if (wcr != null) {
	        return wcr;
	    } else {
	        return null;
	    }
	}
	
	//count total number of users use the suggestion
	 @GetMapping("/SuggestedPlan")
	 public Integer getCountofSuggetionUser() {
		 File f=new File("C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\plans");
		 File list[]=f.listFiles();
		 int count=0;
		 for(int i=0;i<list.length;i++)
		 {
			 if(list[i].isFile())
			 {
				 count++;
			 }
		 }
		 return count;
	 }
	 
	 
	 //Approve user status
	 @PutMapping("/updateUserStatus/{userid}/{status}")
		public String updateUserStatus(@PathVariable("userid") Integer userid,@PathVariable("status") Integer status) {
			boolean b=adminservice.updateUserStatus(userid,status);
			return b?"updateUserStatus updated success...":"updateUserStatus not present...";
		}
	 @GetMapping("/getrequesteduser")
	public List getAllRequestedUser() {
		 List list=adminservice.getrequesteduser();
		return list;
	}
}
