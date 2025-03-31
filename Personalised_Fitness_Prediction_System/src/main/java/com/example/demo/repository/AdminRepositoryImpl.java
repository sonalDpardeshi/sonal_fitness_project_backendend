package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User;

@Repository("adminrepo")
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	JdbcTemplate template;
	
	@Override
	public boolean validateAdmin(String username, String password) {
		return (username.equals("admin")&& password.equals("12345"))?true:false;
	}

	@Override
	public List<User> view() {
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
	
	

}
