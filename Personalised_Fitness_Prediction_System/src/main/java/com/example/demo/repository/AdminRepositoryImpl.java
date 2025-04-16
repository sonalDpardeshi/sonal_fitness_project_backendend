package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User;
import com.example.demo.model.Workout;
import com.example.demo.model.WorkoutCaloriesRelation;

@Repository("adminrepo")
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	JdbcTemplate template;
	
	@Override
	public boolean validateAdmin(String username, String password) {
		return (username.equals("admin")&& password.equals("12345"))?true:false;
	}

	@Override
	public List<User> viewUsers() {
		List<User> list=new ArrayList<>();
		list=template.query("select * from user", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user=new User();
				user.setUserid(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setHeight(rs.getDouble(5));
				user.setWeight(rs.getDouble(6));
				return user;
			}
		});
		
		return list;
	}

	@Override
	public boolean suggest(Integer userid) {
	    // CSV file path where user's workout details are saved
	    String csvFile = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_" + userid + ".csv";
	    
	    // Output file where the suggested plan will be saved
	    String outputFile = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\plans\\suggested_plan_user_" + userid + ".txt";

	    // Lists to hold data from CSV
	    List<Integer> workoutIds = new ArrayList<>();
	    List<Integer> intensityIds = new ArrayList<>();
	    List<Integer> durations = new ArrayList<>();

	    File file = new File(csvFile);
	    if (!file.exists()) {
	        System.out.println("CSV not found for user " + userid);
	        return false;
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] data = line.split(",");
	            if (data.length >= 4) {
	                workoutIds.add(Integer.parseInt(data[1].trim()));
	                intensityIds.add(Integer.parseInt(data[2].trim()));
	                durations.add(Integer.parseInt(data[3].trim()));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    // 🧠 Basic Suggestion Logic (example)
	    int avgDuration = durations.stream().mapToInt(Integer::intValue).sum() / durations.size();
	    int mostCommonWorkout = workoutIds.get(0); // Replace with real logic
	    int mostCommonIntensity = intensityIds.get(0); // Replace with real logic

	    // 💡 Create a suggestion message
	    StringBuilder suggestion = new StringBuilder();
	    suggestion.append("Personalized Fitness Plan for User ").append(userid).append("\n\n");
	    suggestion.append("Based on your recent activity, here is your plan:\n");
	    suggestion.append("- Recommended Workout Type ID: ").append(mostCommonWorkout).append("\n");
	    suggestion.append("- Suggested Intensity Level ID: ").append(mostCommonIntensity).append("\n");
	    suggestion.append("- Average Duration: ").append(avgDuration).append(" minutes\n\n");
	    suggestion.append("👉 Keep up the great work! Try to be consistent with this plan for the next 2 weeks.");

	    // 📝 Save the suggestion to a file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
	        bw.write(suggestion.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }

	    System.out.println("Plan suggested and saved for user " + userid);
	    return true;
	}

//	Workouts are added by admin
	@Override
	public boolean addWorkouts(Workout workout) {
		int value=template.update("insert into workout_type values('0',?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, workout.getWorkout_type_name());
			}
		});
		return value>0?true:false;
		
	}

	@Override
	public List<Workout> viewWorkouts() {
		List<Workout> list=template.query("select * from workout_type", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Workout workout=new Workout();
				workout.setWorkout_type_id(rs.getInt(1));
				workout.setWorkout_type_name(rs.getString(2));
				return workout;
			}
		});
		return list;
	}

	@Override
	public boolean add(WorkoutCaloriesRelation workoutcalories) {
		int value=template.update("insert into WorkoutCaloriesRelation values('0',?,?,?,?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, workoutcalories.getWorkout_type_id());
				ps.setInt(2, workoutcalories.getIntensityid());
				ps.setInt(3, workoutcalories.getDuration());
				ps.setDouble(4, workoutcalories.getCalories_burn());
			}
		});
			return value>0?true:false;
	}

	@Override
	public List<WorkoutCaloriesRelation> view() {
		List<WorkoutCaloriesRelation> list=template.query("select * from WorkoutCaloriesRelation", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkoutCaloriesRelation workoutcalories=new WorkoutCaloriesRelation();
				workoutcalories.setRecordid(rs.getInt(1));
				workoutcalories.setWorkout_type_id(rs.getInt(2));
				workoutcalories.setIntensityid(rs.getInt(3));
				workoutcalories.setDuration(rs.getInt(4));
				workoutcalories.setCalories_burn(rs.getDouble(5));
				return workoutcalories;
			}
		});
		return list;
	}

	@Override
	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid) {
int value=template.update("update WorkoutCaloriesRelation set workout_type_id=?,intensityid=?,duration=?,calories_burn=? where recordid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, workoutcalories.getWorkout_type_id());
				ps.setInt(2, workoutcalories.getIntensityid());
				ps.setInt(3, workoutcalories.getDuration());
				ps.setDouble(4, workoutcalories.getCalories_burn());
				ps.setInt(5,recordid);
				
			}
		});
		return value>0?true:false;
	}

	@Override
	public boolean delete(Integer recordid) {
int value=template.update("delete from WorkoutCaloriesRelation where recordid=?",new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, recordid);
			}
		});
		return value>0?true:false;
	}

	@Override
	public boolean deleteUser(Integer userid) {
		
		int value=template.update("delete from user where userid=?",new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1,userid);
			}
		});
		return value>0?true:false;
	}
	
	

}
