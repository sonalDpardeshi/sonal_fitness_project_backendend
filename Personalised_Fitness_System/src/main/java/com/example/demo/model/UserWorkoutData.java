package com.example.demo.model;
import lombok.Data;

@Data
public class UserWorkoutData {
	
//	uer workout form data fields 
	private int userid;
	private int workout_type_id;
	private int intensityid;
	private int duration;
	private double calories_burn;	
}