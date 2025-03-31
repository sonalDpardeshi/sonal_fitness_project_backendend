package com.example.demo.repository;

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

@Repository("userrepo")
public class UserRepositoryImpl implements UserRepository{

	@Autowired
	JdbcTemplate template;
	List<User> list=null;
	
	@Override
	public boolean add(User user) {
		System.out.println("Repo: "+user.getHeight());
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
		System.out.println("repo: "+value);
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

}
