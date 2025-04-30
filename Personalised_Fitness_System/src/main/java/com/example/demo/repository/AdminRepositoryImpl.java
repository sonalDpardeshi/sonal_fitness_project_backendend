package com.example.demo.repository;

import java.io.*;
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
import com.example.demo.model.*;

@Repository("adminrepo")
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	JdbcTemplate template;
	
	 private String username = "admin";
	 private String password = "12345";
	
	@Override
	public boolean validateAdmin(String username, String password) {
		return (this.username.equals(username)&& this.password.equals(password))?true:false;
	}

	@Override
	public boolean resetAdminCredentials(Admin admin) {
		Admin ad=new Admin();
		this.username=admin.getUsername();
		this.password=admin.getPassword();
		ad.setUsername(username);
		ad.setPassword(password);
		return true;
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

//	View intensities
	@Override
	public List<Intensity> viewIntensities() {
		List<Intensity> list=template.query("select *from intensity", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Intensity i=new Intensity();
			i.setIntensityid(rs.getInt(1));
			i.setIntensity_type(rs.getString(2));
				return i;
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
				ps.setInt(1,Integer.parseInt( workoutcalories.getWorkout_type_id()));
				ps.setInt(2, Integer.parseInt(workoutcalories.getIntensityid()));
				ps.setInt(3, workoutcalories.getDuration());
				ps.setDouble(4, workoutcalories.getCalories_burn());
			}
		});
			return value>0?true:false;
	}

	@Override
	public List<WorkoutCaloriesRelation> view() {
		RowMapper<WorkoutCaloriesRelation> row=new RowMapper<WorkoutCaloriesRelation>() {
			
			@Override
			public WorkoutCaloriesRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorkoutCaloriesRelation wcr=new WorkoutCaloriesRelation();
				wcr.setRecordid(rs.getInt(3));
				wcr.setWorkout_type_id(rs.getString(9));
				wcr.setIntensityid(rs.getString(2));
				wcr.setDuration(rs.getInt(6));
				wcr.setCalories_burn(rs.getDouble(7));
				return wcr;
			}
		};
		List<WorkoutCaloriesRelation> list=template.query("select * from intensity i inner join WorkoutCaloriesRelation wcr on i.intensityid=wcr.intensityid inner join workout_type w on w.workout_type_id=wcr.workout_type_id", row);
		return list;
	}

//	only updates duration and calories burn not workout_id and intensity_id
	@Override
	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer recordid) {
		int value=template.update("update WorkoutCaloriesRelation set workout_type_id=?,intensityid=? ,duration=?,calories_burn=? where recordid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				
				//for getting workout_type_id from workout_type_name
				int workout_type_id=template.queryForObject("select workout_type_id from workout_type where workout_type=?", new Object[] {workoutcalories.getWorkout_type_id()}, Integer.class);
//				System.out.println("repo: "+workout_type_id);
				ps.setString(1,String.valueOf(workout_type_id));//Integer to string conversion
				
				//for intensity
				int intensityid=template.queryForObject("select intensityid from intensity where intensity_type=?", new Object[] {workoutcalories.getIntensityid()}, Integer.class);
//				System.out.println("repo: "+workout_type_id);
				ps.setString(2,String.valueOf(intensityid));//Integer to string conversion
				
				ps.setInt(3, workoutcalories.getDuration());
				ps.setDouble(4, workoutcalories.getCalories_burn());
				ps.setInt(5,recordid);	
			}
		});
		return value>0?true:false;
	}

//	search
	@Override
	public List<WorkoutCaloriesRelation> search(String pattern) {
		List<WorkoutCaloriesRelation> list=template.query("select workout_type,intensity_type,duration,calories_burn from intensity i inner join WorkoutCaloriesRelation wcr on i.intensityid=wcr.intensityid inner join workout_type w on w.workout_type_id=wcr.workout_type_id where workout_type like ? or intensity_type like ? or duration like ? or calories_burn like ?", new PreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1,pattern+"%");
						ps.setString(2,pattern+"%");
						ps.setString(3,pattern+"%");
						ps.setString(4,pattern+"%");
					}
				}, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						WorkoutCaloriesRelation wcr=new WorkoutCaloriesRelation();
						wcr.setWorkout_type_id(rs.getString(1));
						wcr.setIntensityid(rs.getString(2));
						wcr.setDuration(rs.getInt(3));
						wcr.setCalories_burn(rs.getDouble(4));
						return wcr;
					}
				});
				return list.isEmpty()?null:list;
	}
	
	
	
//	Not required 
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

	String filebaseurl="src/main/resources/static/userHistory/";
	String filename=filebaseurl+"user_"+userid+".csv";

	
//	To store workoutid,intensityid,duration 
	List<Integer> workoutids=new ArrayList<>();
	List<Integer> intensityids=new ArrayList<>();
	List<Integer> durations=new ArrayList<>();
	
	
		File f=new File(filename);
		if(!f.exists()) { return null;}
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
		LinkedHashSet<String> lhsin=new LinkedHashSet<>();
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
					lhsin.add(rs.getString(1));
					return lhsin;
				}
			});	
		}
		
//		Logic for workouts
		LinkedHashSet<String> lhswo=new LinkedHashSet<>();
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
					lhswo.add(rs.getString(1));
					return lhswo;
				}
			});	
		}
		
//		adding workouts, intensities,averageduration in map
		Map<String, LinkedHashSet<String>> plan=new LinkedHashMap<String,LinkedHashSet<String>>();
		if(!lhswo.isEmpty()) {plan.put("workouts", lhswo);}
		else{lhswo.add("no recommended any workouts");}
		
		lhswo.forEach(e->System.out.println("repo: "+e+" "));
		
		if(!lhsin.isEmpty()) {plan.put("intensties", lhsin);}
		else{lhsin.add("no recommended any intensity");}
		
		
		LinkedHashSet<String> lhs=new LinkedHashSet<>();
		lhs.add(avgduration+"");
		
		plan.put("durationPerDay",lhs);
//		System.out.println("map size: "+plan.size());
		return plan;
	}

//	search workouts
	@Override
	public List<Workout> searchWorkouts(String pattern) {
		List<Workout> workoutlist=template.query("select * from workout_type where workout_type like ?", new Object[] {pattern+"%"}, new RowMapper<Workout>() {

			@Override
			public Workout mapRow(ResultSet rs, int rowNum) throws SQLException {
			Workout w=new Workout();
			w.setWorkout_type_id(rs.getInt(1));
			w.setWorkout_type_name(rs.getString(2));
				return w;
			}
		});
		return workoutlist.isEmpty()?null:workoutlist;

//		//for intensity
//		List<Intensity> intensitylist=template.query("select * from intensity where intensity_type like ?", new Object[] {pattern+"%"}, new RowMapper<Intensity>() {
//
//			@Override
//			public Intensity mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Intensity i=new Intensity();
//				i.setIntensityid(rs.getInt(1));
//				i.setIntensity_type(rs.getString(2));
//				return i;
//			}
//		});
//		return intensitylist.isEmpty()?null:intensitylist;
	}

	@Override
	public List<User> searchUsers(String pattern) {
		String p=pattern+"%";

		String query="select * from user where name like ? or email like ? or cast(height as char) like ? or cast(weight as char)like ?";
		List<User> list=template.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1,p);
			ps.setString(2,p);
			ps.setString(3, p);
			ps.setString(4,p);
			
			}
		}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			User u=new User();
			u.setName(rs.getString(2));
			u.setEmail(rs.getString(3));
			u.setHeight(rs.getDouble(5));
			u.setWeight(rs.getDouble(6));
				return u;
			}
		});
		return list;
	}	

}