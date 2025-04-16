package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Intensity;
import com.example.demo.model.User;

@Repository("intensityRepo")
public class IntensityRepoImpl implements IntensityRepo {

	@Autowired
	JdbcTemplate template;
	@Override
	public List<Intensity> viewIntensity() {
		List<Intensity> list=new ArrayList<>();
		list=template.query("select * from intensity", new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Intensity in=new Intensity();
				in.setIntensityid(rs.getInt(1));
				in.setIntensity_type(rs.getString(2));
				return in;
			}
		});
		
		return list;

	}

}
