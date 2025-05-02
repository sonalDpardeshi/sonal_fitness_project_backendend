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
import java.util.stream.Collectors;

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
		return (username.equals("admin")&& password.equals("123456"))?true:false;
	}

	//approve user or Reject
	
	
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
				user.setStatuss(rs.getString(7));
				return user;
			}
		});
		
		return list;
	}

	@Override
	public boolean suggest(Integer userid) {
	    String csvFile = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_" + userid + ".csv";
	    String outputFile = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\plans\\suggested_plan_user_" + userid + ".txt";

	    class WorkoutEntry {
	        int workoutTypeId;
	        int intensityId;
	        int duration;

	        WorkoutEntry(int workoutTypeId, int intensityId, int duration) {
	            this.workoutTypeId = workoutTypeId;
	            this.intensityId = intensityId;
	            this.duration = duration;
	        }

	        double distanceTo(WorkoutEntry other) {
	            int dWorkout = this.workoutTypeId - other.workoutTypeId;
	            int dIntensity = this.intensityId - other.intensityId;
	            int dDuration = this.duration - other.duration;
	            return Math.sqrt(dWorkout * dWorkout + dIntensity * dIntensity + dDuration * dDuration);
	        }
	    }

	    // 1. Read workout history from CSV file
	    List<WorkoutEntry> history = new ArrayList<>();
	    if (new File(csvFile).exists()) {
	        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] data = line.split(",");
	                if (data.length >= 5) {
	                    int workoutId = Integer.parseInt(data[1].trim());
	                    int intensityId = Integer.parseInt(data[2].trim());
	                    int duration = Integer.parseInt(data[3].trim());
	                    history.add(new WorkoutEntry(workoutId, intensityId, duration));
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false; // Early return on file error
	        }
	    }

	    if (history.size() < 1) {
	        System.out.println("Not enough workout history.");
	        return false;
	    }

	    // 2. Get the latest workout
	    WorkoutEntry latest = history.get(history.size() - 1);

	    // 3. Fetch all workouts from the database
	    String workoutQuery = "SELECT workout_type_id, intensityid, duration FROM workoutcaloriesrelation";
	    
	    List<WorkoutEntry> allWorkouts = template.query(workoutQuery,new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return new WorkoutEntry(rs.getInt("workout_type_id"),rs.getInt("intensityid"),rs.getInt("duration"));
			}
	    	
		});
//	    List<WorkoutEntry> allWorkouts = template.query(
//	            workoutQuery,
//	            (rs, rowNum) -> new WorkoutEntry(
//	                    rs.getInt("workout_type_id"),
//	                    rs.getInt("intensityid"),
//	                    rs.getInt("duration"))
//	    );

	    if (allWorkouts.isEmpty()) {
	        System.out.println("No workouts available for suggestion.");
	        return false;
	    }

	    // 4. Calculate distance between latest workout and all available workouts
	    Map<WorkoutEntry, Double> distanceMap = new HashMap<>();
	    for (WorkoutEntry option : allWorkouts) {
	        distanceMap.put(option, latest.distanceTo(option));
	    }

	    // 5. Get top 1 closest workout option (or more if needed)
	    WorkoutEntry bestMatch = distanceMap.entrySet().stream()
	            .sorted(Map.Entry.comparingByValue())
	            .map(Map.Entry::getKey)
	            .findFirst()
	            .orElse(null);

	    if (bestMatch == null) {
	        System.out.println("No suitable workout found.");
	        return false;
	    }

	    // 6. Get names from DB
	    String workoutName = template.queryForObject(
	            "SELECT workout_type_name FROM workout_type WHERE workout_type_id = ?",
	            new Object[]{bestMatch.workoutTypeId},
	            String.class
	    );

	    String intensityName = template.queryForObject(
	            "SELECT intensity_type FROM intensity WHERE intensityid = ?",
	            new Object[]{bestMatch.intensityId},
	            String.class
	    );

	    // 7. Write to suggestion file
	    StringBuilder suggestion = new StringBuilder();
	    suggestion.append("📅 Date: ").append(java.time.LocalDate.now()).append("\n");
	    suggestion.append("- Recommended Workout: ").append(workoutName).append("\n");
	    suggestion.append("- Suggested Intensity Level: ").append(intensityName).append("\n");
	    suggestion.append("- Suggested Duration: ").append(bestMatch.duration).append(" minutes\n\n");
	    suggestion.append("💪 Keep going strong! Here's your next recommended workout.");

	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
	        bw.write(suggestion.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }

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

	@Override
	public boolean updateUserStatus(Integer userid, Integer status) {
	    String statusStr;
	    if (status == 1) {
	        statusStr = "Approve";
	    } else if (status == -1) {
	        statusStr = "Reject";
	    } else {
	        statusStr = "Pending";
	    }

	    int value = template.update("UPDATE user SET Status=? WHERE userid=?", ps -> {
	        ps.setString(1, statusStr);
	        ps.setInt(2, userid);
	    });

	    return value > 0;
	}

	@Override
	public List getrequesteduser() {
		// TODO Auto-generated method stub
		  String sql = "SELECT pr.rid , pr.userid, u.name, u.email, pr.status, pr.requested_at " +
                  "FROM plan_request pr " +
                  "JOIN user u ON pr.userid = u.userid " +
                  "WHERE pr.status = 'Requested' " +
                  "ORDER BY pr.requested_at DESC";

     List result = template.queryForList(sql);
     return result;
	}

	

}
