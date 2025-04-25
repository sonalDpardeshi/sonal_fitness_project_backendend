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
				User u=new User();
				u.setEmail(rs.getString(3));
				u.setPassword(rs.getString(4));
				return u;			
				}
		};
		 List<User> userlist = template.query("SELECT * FROM user where email=? and password=?", pstmt, row);
//		System.out.println("value: "+userlist.size());
		return !userlist.isEmpty();
	}

	
	@Override
	public int findUserIdByEmail(String email) {
		List<User> list=template.query("select userid from user where email=?", new Object[] {email}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u=new User();
				u.setUserid(rs.getInt(1));
				return u;
			}
		});
		return list.get(0).getUserid();
	}
	
//	find name of user who is currently logged in
	@Override
	public String findNameByEmail(String email) {
		String name=template.queryForObject("select name from user where email=?", new Object[] {email}, String.class);
		return name;
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
//			System.out.println();
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
//		String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userid+".csv";
		String filebaseurl="src/main/resources/static/userHistory/";
		String filename=filebaseurl+"user_"+userid+".csv";

		File f=new File(filename);
		if(!f.exists()) {
			return "file not found ";
		}
		
		String str="";
		StringBuilder sb=new StringBuilder();
		try(BufferedReader br=new BufferedReader(new FileReader(f))){
			while((str=br.readLine())!=null) {
				String s[]=str.split(",");
				int workout_type_id=Integer.parseInt(s[1]);
				
//				getting workout name from workout id
//				If our query returns single record then we use queryForObject method and acc to that return type we use .class so we don't require rowMapper 
				String workout_type_name=template.queryForObject("select workout_type from workout_type where workout_type_id=?", new Object[] {workout_type_id}, String.class);
//				System.out.println("workout type name"+workout_type_name.get(0));
				
//				getting intensity type from intensityid
				int intensityid=Integer.parseInt(s[2]);
				String intensity_type_name=template.queryForObject("select intensity_type from intensity where intensityid=?", new Object[] {intensityid},String.class);
//				System.out.println("intensity type name"+intensity_type_name.get(0));
				
				int duration=Integer.parseInt(s[3]);
				double calories_burn=Double.parseDouble(s[4]);
				
				 sb.append(workout_type_name).append(" | ").append(intensity_type_name).append(" | ").append(duration).append(" | ").append(calories_burn).append("\n");
			}
		}
		catch(Exception e) { 
			e.printStackTrace();
		}
//		System.out.println("repo: "+sb);
		return sb.toString();
	}

	@Override
	public double totalCount(int userid) {
//String filename="D:\\Fitness_Project\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\UserHistory\\user_"+userid+".csv";
		String filebaseurl="src/main/resources/static/userHistory/";
		String filename=filebaseurl+"user_"+userid+".csv";

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
			e.printStackTrace();
		}
//		System.out.println(total);
		return total;
	}

//	adding csv file name in db
	@Override
	public boolean addpath(int userid,String filename) {

		Integer val=template.queryForObject("select count(*) from userhistory where userid=?", new Object[] {userid},Integer.class);
		int v=0;
		if(val>0) {
			 v=template.update("update userhistory set filepath=? where userid=?", new Object[] {filename,userid});
			if(v>0)
			{
		System.out.println("csv file updated in userhistory table");
		}
			else {
				System.out.println("csv file not updated in userhistory table");
			}
			}
		else {
			 v=template.update("insert into userhistory values('0',?,?)", new Object[] {userid,filename});
			if(v>0) {
				System.out.println("new csv created in userhistory table  ");	
			}
		}	
		return v>0?true:false;
	}
	
//	Retrieve csv file path for download to user
	@Override
	public String getFilePath(Integer userid) {
		String fileBaseUrl = "http://localhost:8080/userHistory/";
		String filePath = "";

		try{
			filePath=template.queryForObject("select filepath from userHistory where userid=?",new Object[] {userid},String.class);
		}
		catch(Exception e) {
//			e.printStackTrace();
//			System.out.println("no file found for this userid");
			return null;
		}
		
//		System.out.println("Filpath in repo: "+ s);
		if(filePath==null||filePath.trim().isEmpty()) {
		}
		System.out.println("Repo file path retrieve : "+(fileBaseUrl+filePath));
		
		return (fileBaseUrl+filePath);//returning file path
	}

	

//	View Workouts
	@Override
	public List<Workout> viewWorkouts() {
		List<Workout> list=template.query("select * from workout_type", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Workout w=new Workout();
				w.setWorkout_type_name(rs.getString(2));
				return w;
			}
		});
		return list;
	}

	
//	Update user profile
	@Override
	public boolean updateProfile(User user,int userid) {
		int value=template.update("update user set name=?,email=?,password=?,height=?,weight=? where userid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getPassword());
				ps.setDouble(4, user.getHeight());
				ps.setDouble(5, user.getWeight());
				ps.setInt(6, userid);
			}
		});
		return value>0?true:false;
	}


	@Override
	public List<User> viewProfile(Integer userid) {
		List<User> list=template.query("select * from user where userid=?",new Object[] {userid},new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u=new User();
				u.setUserid(rs.getInt(1));
				u.setName(rs.getString(2));
				u.setEmail(rs.getString(3));
				u.setPassword(rs.getString(4));
				u.setHeight(rs.getDouble(5));
				u.setWeight(rs.getDouble(6));
				return u;
			}
		});
		return list;
	}
}
