package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import com.example.demo.model.UserWorkoutData;

@Repository("userrepo")
public class UserRepositoryImpl implements UserRepository{

	@Autowired
	JdbcTemplate template;
	
	List<User> list=null;
	
	@Override
	public boolean add(User user) {
//		System.out.println("Repo: "+user.getHeight());
		int value=template.update("insert into user values('0',?,?,?,?,?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setDouble(4, user.getHeight());
			ps.setDouble(5, user.getWeight());
			}
		});
//		System.out.println("repo: "+value);
		
		return value>0?true:false;
	}

//	validate user
	@Override
	public boolean validate(String username, String password) {
		PreparedStatementSetter pstmt=new PreparedStatementSetter() {	
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, username);
				ps.setString(2, password);
			}
		};
		
		RowMapper row=new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return null;
			}
		};
		int value=template.query("select * from user where email=? and password=?", pstmt, row).size();
//		System.out.println("value: "+value);
		return value>0?true:false;
	}

//	add workouts in csv
	

	@Override
	public boolean fetch(UserWorkoutData userworkout) {
		
		List value=template.query("select userid from user where userid=?", new Object[] {userworkout.getUserid()}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u=new User();
				u.setUserid(rs.getInt(1));
				return u;
			}
		});
		
		if(value.size()==0) {
			System.out.println();
			return false;
		}
		int userId=userworkout.getUserid();
		int duration=userworkout.getDuration();
		double calories=0;
		
		List<UserWorkoutData> list=template.query("select * from workoutcaloriesrelation where workout_type_id=? and intensityid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setInt(1, userworkout.getWorkout_type_id());
			ps.setInt(2, userworkout.getIntensityid());
			}
		}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserWorkoutData uwdata=new UserWorkoutData();
			uwdata.setUserid(userId);
			uwdata.setWorkout_type_id(rs.getInt(2));
			uwdata.setIntensityid(rs.getInt(3));
			uwdata.setDuration(duration);
			
			int d=rs.getInt(4);
			double cal=rs.getDouble(5);
			
//			predicting calories logic
			uwdata.setCalories_burn((duration*cal)/d);
			return uwdata;
			}
		});
		list.forEach(e->
		userworkout.setCalories_burn(e.getCalories_burn()));
		
		
				return list.size()>0?true:false;
	}

	
//	read user workout history
	@Override
	public String readHistory(int userid) {
		String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userid+".csv";
		
		File f=new File(filename);
		if(!f.exists()) {
			return "file not found";
		}
		
		String str="";
		StringBuilder sb=new StringBuilder();
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			while((str=br.readLine())!=null) {
				 sb.append(str).append("\n");
			}
		}
		catch(Exception e) {
			
		}
		return sb.toString();
	}

	@Override
	public double totalCount(int userid) {
String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userid+".csv";
		
		File f=new File(filename);
		if(!f.exists()) {
			return 0;
		}
		
		String str="";
		StringBuilder sb=new StringBuilder();
		double total=0;
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			while((str=br.readLine())!=null) {
//				count total calories remaining
				String s[]=str.split(",");
				total+=Double.parseDouble(s[s.length-1]);//Converting string into double
//				System.out.print("repo: "+s[s.length-1]);
				 sb.append(str).append("\n");
			}
		}
		catch(Exception e) {
			
		}
		System.out.println(total);
		return total;
	}

//	adding csv file name in db
	@Override
	public boolean addpath(int userid,File f) {
		System.out.println(f.getAbsoluteFile());
		Integer val=template.queryForObject("select count(*) from userhistory where userid=?", new Object[] {userid},Integer.class);
		int v=0;
		if(val>0) {
			 v=template.update("update userhistory set filepath=? where userid=?", new Object[] {f.getAbsolutePath(),userid});
			if(v>0)
			{
		System.out.println("csv updated in userhistory table");
		}
			else {
				System.out.println("csv not updated in userhistory table");
			}
			}
		else {
			 v=template.update("insert into userhistory values('0',?,?)", new Object[] {userid,f.getAbsolutePath()});
			
			if(v>0) {
				System.out.println("new csv created in userhistory table  ");	
			}
		}	
		return v>0?true:false;
	}


}
