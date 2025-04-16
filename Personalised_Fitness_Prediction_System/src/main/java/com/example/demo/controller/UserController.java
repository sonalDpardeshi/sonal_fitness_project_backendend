package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@CrossOrigin(origins = "http://localhost:5173")
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
	
	//User update profile here
		@PutMapping("/update/{userid}")
		public String userUpdate(@PathVariable int userid,@RequestBody User user) {
			
			boolean b=userservice.updateUser(userid,user);
			return b?"Profile Update Successfully":"Profile not updated";
		}

		  @GetMapping("/getuser/{email}")
		    public User getUserByEmail(@PathVariable String email) {
		        User user = userservice.getUserByEId(email);
		        if (user != null) {
		            return user; // returns name, email, etc.
		        } else {
		            return null;
		        }
		    }
		
		
	
//	user fill workout form
	@PostMapping("/workoutdetails")
	public String workoutDetails(@RequestBody UserWorkoutData userworkout) throws IOException {
		boolean b=userservice.fetch(userworkout);
		//System.out.println("User is not getting"+b);
   if(b){
		String filename="C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_"+userworkout.getUserid()+".csv";
				
		File f=new File(filename);
		if(!f.exists()) {f.createNewFile();}

//		adding csv file name in db
		boolean b1=userservice.addPath(userworkout.getUserid(),f);
		System.out.println(b1);
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
	
//	profile update and view
	
//	

}
