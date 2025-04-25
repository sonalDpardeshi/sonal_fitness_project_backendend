package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;
import com.example.demo.Exceptions.*;
import com.example.demo.model.*;
import com.example.demo.service.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:5173")
public class UserController {

    // Service object
    @Autowired
    UserService userservice;

    // User register
    @PostMapping("/register")
    public String userRegister(@RequestBody User user) {
        boolean b = userservice.add(user);
        if (b) {
            return "User registration success....";
        } else {
            throw new CustomAppException("User registration failed");
        }
    }

    @PostMapping("/login")
    public Map<String, Object> userLogin(@RequestBody User user) {
        boolean b = userservice.validate(user.getEmail(), user.getPassword());
        Map<String, Object> m = new HashMap<>();
        if (b) {
            int userid = userservice.findUserIdByEmail(user.getEmail());
            String name = userservice.findeNameByEmail(user.getEmail());
            m.put("userid", userid);
            m.put("username", user.getEmail());
            m.put("name", name);
            m.put("msg", "User login success....");
        } else {
            // Throw the exception, which will be handled by the GlobalExceptionHandler
            throw new UserNotFoundException("Please check username or password");
        }
        return m;
    }


    // User fill workout form
    @PostMapping("/workoutdetails")
    public String workoutDetails(@RequestBody UserWorkoutData userworkout) throws IOException {
        boolean b = userservice.fetch(userworkout);
        if (b) {
            String filebaseurl = "src/main/resources/static/userHistory/";
            String filename = filebaseurl + "user_" + userworkout.getUserid() + ".csv";

            File f = new File(filename);
            if (!f.exists()) {
                f.createNewFile();
            }

            filename = "user_" + userworkout.getUserid() + ".csv";
            boolean b1 = userservice.addPath(userworkout.getUserid(), filename);

            FileWriter fw = new FileWriter(f, true);
            fw.write(userworkout.getUserid() + "," + userworkout.getWorkout_type_id() + ","
                    + userworkout.getIntensityid() + "," + userworkout.getDuration() + ","
                    + userworkout.getCalories_burn() + "\n");
            fw.close();
            return "workout details added to csv file..";
        } 
        else {
            throw new CustomAppException("User with this userid is not registered");
        }
    }

    // Retrieve CSV file path from db
    @GetMapping("/getfilepath/{userid}")
    public String downloadCSVFile(@PathVariable("userid") Integer userid) {
        String f = userservice.getFilePath(userid);
        if (f == null || f.isEmpty()) {
            throw new CustomAppException("File path not found for user " + userid);
        }
        System.out.println("controller: "+f);
        return f;
    }

    // See history of individual user
    @GetMapping("/read/{userid}")
    public String readCsv(@PathVariable("userid") Integer userid) throws IOException {
        String s = userservice.readHistory(userid);
        if (s == null || s.isEmpty()) {
            throw new CustomAppException("No history found for user " + userid);
        }
        return s;
    }

    // Calculate total calories burned uptill now
    @GetMapping("/getcaloriesuptillnow/{userid}")
    public double getCaloriesBurnUptillNow(@PathVariable("userid") Integer userid) {
        double d = userservice.totalcount(userid);
        if (d < 0) {
            throw new CustomAppException("Error calculating total calories for user " + userid);
        }
        return d;
    }

    // View workouts
    @GetMapping("/workout/view")
    public List<Workout> viewWorkouts() {
        List<Workout> list = userservice.viewWorkouts();
        if (list == null || list.isEmpty()) {
            throw new CustomAppException("No workouts available");
        }
        return list;
    }

    // Update profile
    @PutMapping("/updateprofile/{userid}")
    public String updateProfile(@RequestBody User user, @PathVariable("userid") Integer userid) {
        boolean b = userservice.updateProfile(user, userid);
        if (b) {
            return "profile updated success...";
        } else {
            throw new CustomAppException("Profile update failed for user " + userid);
        }
    }

    // View profile
    @GetMapping("/viewprofile/{userid}")
    public List<User> viewProfile(@PathVariable("userid") Integer userid) {
        List<User> list = userservice.viewProfile(userid);
        if (list == null || list.isEmpty()) {
            throw new CustomAppException("No profile found for user " + userid);
        }
        return list;
    }
}
