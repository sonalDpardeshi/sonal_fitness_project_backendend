package com.example.demo.model;
import lombok.Data;

@Data
public class WorkoutCaloriesRelation {
	
	private int recordid;
	private int workout_type_id;
	private int intensityid;
	private int duration;
	private double calories_burn;
	

}
