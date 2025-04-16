package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Workout;

public interface WorkoutService {

	public boolean add(Workout workout);

	public List<Workout> view();
   
}
