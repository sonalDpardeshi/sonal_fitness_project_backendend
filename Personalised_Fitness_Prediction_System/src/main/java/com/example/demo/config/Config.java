package com.example.demo.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class Config {
	
	@Bean("template")
	public JdbcTemplate getTemplate(DataSource datasource) {
		JdbcTemplate template=new JdbcTemplate();
		return template;
	}

}
