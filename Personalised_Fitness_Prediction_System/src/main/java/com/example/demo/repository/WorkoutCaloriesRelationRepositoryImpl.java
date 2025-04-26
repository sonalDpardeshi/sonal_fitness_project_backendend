package com.example.demo.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.model.WorkoutCaloriesRelation;

@Repository("WorkoutCaloriesRelationrepo")
public class WorkoutCaloriesRelationRepositoryImpl implements WorkoutCaloriesRelationRepository{

	@Autowired
	JdbcTemplate template;
	
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
	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer record_id) {
		int value=template.update("update WorkoutCaloriesRelation set workout_type_id=?,intensityid=?,duration=?,calories_burn=? where recordid=?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, workoutcalories.getWorkout_type_id());
				ps.setInt(2, workoutcalories.getIntensityid());
				ps.setInt(3, workoutcalories.getDuration());
				ps.setDouble(4, workoutcalories.getCalories_burn());
				ps.setInt(5,record_id);
				
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
	public WorkoutCaloriesRelation getworkoutcalbyrid(Integer recordid) {
		
		String qry="select * from WorkoutCaloriesRelation where recordid=?";
		return template.queryForObject(qry,new BeanPropertyRowMapper<>(WorkoutCaloriesRelation.class),recordid);
	}

}
