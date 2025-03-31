package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.WorkoutCaloriesRelation;

public interface WorkoutCaloriesRelationRepository {

	public boolean add(WorkoutCaloriesRelation workoutcalories);

	public List<WorkoutCaloriesRelation> view();

	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer record_id);

	public boolean delete(Integer recordid);

}
