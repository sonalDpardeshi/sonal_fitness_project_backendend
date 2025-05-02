package com.example.demo.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
		int value=template.update("INSERT INTO user (name ,email,password,height,weight,status) VALUES (?,?,?,?,?,?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setDouble(4, user.getHeight());
			ps.setDouble(5, user.getWeight());
			ps.setString(6, "Pending");
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
				ps.setString(3,"Approve");
			}
		};
		
		RowMapper row=new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return null;
			}
		};
		int value=template.query("select * from user where email=? and password=? and status=?", pstmt, row).size();

		return value>0?true:false;
	}

//	add workouts in csv
	@Override
	public boolean updateUser(int id,User user) {
	    int value = template.update(
	        "UPDATE user SET name = ?,email=?, password = ?, height = ?, weight = ?   WHERE userid = ?",
	        new PreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	                ps.setString(1, user.getName());
	                ps.setString(2, user.getEmail());
	                ps.setString(3, user.getPassword());
	                ps.setDouble(4, user.getHeight());
	                ps.setDouble(5, user.getWeight()); 
	                ps.setInt(6, id);// email as identifier
	            }
	        });

	    return value > 0;
	}

	

	@Override
	public boolean fetch(UserWorkoutData userworkout) {
	//	System.out.println("repo"+userworkout.getIntensityid());
		List value=template.query("select userid from user where userid=?", new Object[] {userworkout.getUserid()}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u=new User();
				u.setUserid(rs.getInt(1));
				//System.out.println(u.getUserid());
				return u;
			}
		});
		
		if(value.size()==0) {
			System.out.println("Record is not present");
			return false;
		}
		int userId=userworkout.getUserid();
		int duration=userworkout.getDuration();

		double calories=0;
		
		List<UserWorkoutData> list=template.query("select * from workoutcaloriesrelation where workout_type_id=? and intensityid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setInt(1, userworkout.getWorkout_type_id());
		//	System.out.println("woroutid "+userworkout.getWorkout_type_id());
			ps.setInt(2, userworkout.getIntensityid());
		//	System.out.println("intensityid "+ userworkout.getIntensityid());
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
		String filename="C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_"+userid+".csv";
		
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

	// Calories Burned=Duration (minutes)×MET×3.5×weight (kg)/200
	@Override
	public double totalCount(int userid) {
    String filename="C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_"+userid+".csv";
	String str="";	
    File f=new File(filename);
    double total=0;
    if(f.exists())
    {
    	try(BufferedReader br=new BufferedReader(new FileReader(f))){
    		while((str=br.readLine())!=null)
    		{
    			String s[]=str.split(",");
    			total+=Double.parseDouble(s[s.length-1]);
    		}
    	}
    	catch(Exception e)
    	{
    	e.printStackTrace();
    	}
    	//System.out.println("Total Calories Burn ... "+total);
    }
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

@Override
public User getUserByEId(String email) {
	String qry="select * from User where email=?";
	return template.queryForObject(qry,new BeanPropertyRowMapper<>(User.class),email);
}

@Override
public String viewSuggestedPlan(Integer userid) throws IOException {
    String filename = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\plans\\suggested_plan_user_" + userid + ".txt";
    File file = new File(filename);
    StringBuilder content = new StringBuilder();

    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    } else {
        return "Suggested plan not found for user ID: " + userid;
    }
}

@Override
public String requestforplan(int userid) throws Exception {
    String filename = "C:\\Fitness_Prediction\\BackEnd\\Fitness_Backend\\New_Fitness_Backend\\sonal_fitness_project_backendend\\Personalised_Fitness_Prediction_System\\src\\main\\resources\\static\\csvfile\\user_" + userid + ".csv";
    File f = new File(filename);

    if (!f.exists()) {
        return "You have not performed any workout. You cannot make request.";
    }

    int count = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        while (br.readLine() != null) {
            count++;
        }

        if (count < 1) {
            return "You are not eligible for a plan. You cannot make request.";
        } else {
            // Check if already requested
            int existing = template.queryForObject(
                "SELECT COUNT(*) FROM plan_request WHERE userid = ? AND status = 'Requested'", 
                Integer.class, userid
            );

            if (existing > 0) {
                return "Plan already requested.";
            }

            int value = template.update(
                "INSERT INTO plan_request (userid, status, requested_at) VALUES (?, ?, ?)",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, userid);
                        ps.setString(2, "Requested");
                        ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                    }
                }
            );

            if (value > 0) {
                return "Plan request submitted successfully.";
            } else {
                return "Failed to submit request.";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("An error occurred while requesting the plan.");
    }
}

}