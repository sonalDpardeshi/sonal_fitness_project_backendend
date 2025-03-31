package com.example.demo.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Workout;

@Repository("workoutrepo")
public class WorkoutRepositoryImpl implements WorkoutRepository {
	@Autowired
	JdbcTemplate template;
	
	@Override
	public boolean add(Workout workout) {

		int value=template.update("insert into workout_type values('0',?)", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, workout.getWorkout_type_name());
			}
		});
		return value>0?true:false;
	}

	@Override
	public List<Workout> view() {
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

}
