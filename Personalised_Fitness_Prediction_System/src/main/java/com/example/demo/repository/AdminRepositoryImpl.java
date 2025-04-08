package com.example.demo.repository;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.demo.model.*;

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
				user.setName(rs.getString(2));
				user.setEmail(rs.getString(3));
				user.setHeight(rs.getDouble(5));
				user.setWeight(rs.getDouble(6));
				return user;
			}
		});
		return list;
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
	
	
//	recommending plan to user by admin as per userid
	@Override
	public Map<String,LinkedHashSet<String>> suggest(Integer userid) {
	String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userid+".csv";

//	To store workoutid,intensityid,duration 
	List<Integer> workoutids=new ArrayList<>();
	List<Integer> intensityids=new ArrayList<>();
	List<Integer> durations=new ArrayList<>();
	
	
		File f=new File(filename);
		if(!f.exists()) {return null;}
		String str="";
		StringBuilder sb=new StringBuilder();
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			while((str=br.readLine())!=null) {
				String data[]=str.split(",");
				 workoutids.add(Integer.parseInt(data[1]));
				 intensityids.add(Integer.parseInt(data[2]));
				 durations.add(Integer.parseInt(data[3]));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
//		Converting list into arrays
		int wo[]=new int[workoutids.size()];
		for(int i=0;i<workoutids.size();i++) {
			wo[i]=workoutids.get(i);
		}
		
		int in[]=new int[intensityids.size()];
		for(int i=0;i<intensityids.size();i++) {
			in[i]=intensityids.get(i);
		}
		int du[]=new int[durations.size()];
		for(int i=0;i<durations.size();i++) {
			du[i]=durations.get(i);
		}
		
//		Logic for calculating average duration
		int avgduration=0,sum=0;
		for(int i=0;i<du.length;i++) {
			sum+=du[i];	
		}
		avgduration=sum/du.length;

//		Logic for intensities
		LinkedHashSet<String> hsin=new LinkedHashSet<>();
		for(int i=0;i<in.length;i++) {
			int p=in[i];
			List list=template.query("select intensity_type from intensity where intensityid=?", new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, p);
				}
			}, new RowMapper() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					hsin.add(rs.getString(1));
					return hsin;
				}
			});	
		}
		
//		Logic for workouts
		LinkedHashSet<String> hswo=new LinkedHashSet<>();
		for(int i=0;i<wo.length;i++) {
			int p=wo[i];
			List list=template.query("select workout_type from workout_type where workout_type_id=?", new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, p);
				}
			}, new RowMapper() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					hswo.add(rs.getString(1));
					return hswo;
				}
			});	
		}
		
//		adding workouts, intensities,averageduration in map
		Map<String,LinkedHashSet<String>> plan=new LinkedHashMap<String,LinkedHashSet<String>>();
		plan.put("workouts", hswo);
		plan.put("intensties", hsin);
		
		LinkedHashSet<String> lhs=new LinkedHashSet<>();
		lhs.add(avgduration+"");
		plan.put("duration for day",lhs);
		
		return plan;
	}

}
