package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.*;

import com.example.demo.model.*;
import com.example.demo.service.*;

@RestController
@RequestMapping("/user")
public class UserController {
//	service object
	@Autowired
	UserService userservice;
	
	
//	User register
	@PostMapping("/register")
	public String userRegister(@RequestBody User user) {
//		System.out.println("controller: "+user.getEmail());
		boolean b=userservice.add(user);
		if(b) {
		return "User registration success....";
		}
		else {
			return "User registration failed";
		}
	}
	
//	user login 
	@PostMapping("/login")
	public String userLogin(@RequestBody User user){//@RequestParam("username") String username,@RequestParam("password") String password) {
		boolean b=userservice.validate(user.getEmail(),user.getPassword());
		if(b)
		{
			return "User login success....";
		}
		else {
			return "Please check username or password";
		}
	}
	
//	user fill workout form
	@PostMapping("/workoutdetails")
	public String workoutDetails(@RequestBody UserWorkoutData userworkout) throws IOException {
		boolean b=userservice.fetch(userworkout);
		if(b){
		String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userworkout.getUserid()+".csv";
				
		File f=new File(filename);
		if(!f.exists()) {f.createNewFile();}

//		adding csv file name in db
		boolean b1=userservice.addPath(userworkout.getUserid(),f);
//		System.out.println(b1);
		System.out.println("File path in controller: "+f.getAbsolutePath());
		FileWriter fw=new FileWriter(f,true);
//		System.out.println("Controller: "+userworkout.getCalories_burn());
		
	fw.write(userworkout.getUserid()+","+userworkout.getWorkout_type_id()+","+userworkout.getIntensityid()+","+userworkout.getDuration()+","+userworkout.getCalories_burn()+"\n");
				fw.close();
	return "workout details added to csv file..";
		}
		else {
			return "user with this userid is not registered ";
		}
	}

	
//	See history of individual user(means see/read own csv file)
	@GetMapping("/read/{userid}")
	public String readCsv(@RequestBody UserWorkoutData userworkout,@PathVariable Integer userid) throws IOException {
		
		String s=userservice.readHistory(userid);
		
		return s;		
	}
		
//	calculate total calories burn uptill now
	@GetMapping("/getcaloriesuptillnow/{userid}")
	public double getCaloriesBurnUptillNow(@PathVariable("userid") Integer userid) {
		
		double d= userservice.totalcount(userid);
//		System.out.println("Controller: "+d);
		return d;
	}
	
	
//Remaining operations of user
//	view workouts
	@GetMapping("/workout/view")
	public List<Workout> viewWorkouts(){
		List<Workout> list=userservice.viewWorkouts();
		return list;
	}
	
// update profile  
	@PostMapping("/updateprofile/{userid}")
	public String updateProfile(@RequestBody User user,@PathVariable("userid") Integer userid) {
		boolean b=userservice.updateProfile(user,userid);
		if(b) {
		return "profile updated success...";
		}
		else {
			return "profile not updated success...";
		}
	}
	
	
//	view profile  (icon var click and get id and see self profile)
	@GetMapping("/viewprofile")
	public List<User> viewProfile(@RequestParam("userid") Integer userid){
		List<User> list=userservice.viewProfile(userid);
		return list;
	}

}
