package com.example.demo.model;
import lombok.Data;

@Data
public class WorkoutCaloriesRelation {
	
	private int recordid;
	private String workout_type_id;
	private String intensityid;
	private int duration;
	private double calories_burn;
	
}
