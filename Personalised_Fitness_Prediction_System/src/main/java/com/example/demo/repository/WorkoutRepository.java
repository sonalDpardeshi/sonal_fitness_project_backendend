package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.Workout;

public interface WorkoutRepository {

	public boolean add(Workout workout);

	public List<Workout> view();

}
